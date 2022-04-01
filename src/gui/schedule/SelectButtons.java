package gui.schedule;

import data.*;
import gui.components.Dropdown;
import io.FileManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import logging.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectButtons extends Pane {
    private HBox buttons = new HBox();
    //Buttons
    private TextField startTime = new TextField();                                                      //Time
    private TextField endTime = new TextField();

    private Dropdown<Classroom> classRoomSelect = new Dropdown<>();                                     //ComboBoxes
    private MenuButton teacherGroupSelect = new MenuButton("Teacher");
    private Dropdown<Lesson> courseSelect = new Dropdown<>();
    private MenuButton studentGroupSelect = new MenuButton("Class");
    private ScheduleView scheduleView;
    private ArrayList<Teacher> teachers;
    private ArrayList<StudentGroup> students = new ArrayList<>();

    public SelectButtons(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
        this.getChildren().add(this.buttons);
        this.buildSelectButtons();
        this.startTime.setPrefWidth(150);
        this.endTime.setPrefWidth(150);
        this.classRoomSelect.setPrefWidth(150);
        this.teacherGroupSelect.setPrefWidth(150);
        this.courseSelect.setPrefWidth(150);
        this.studentGroupSelect.setPrefWidth(150);
    }

    private void buildSelectButtons() {
        this.startTime.setPromptText("Start time");
        this.endTime.setPromptText("End time");

        this.classRoomSelect.setPromptText("Classroom");
        this.courseSelect.setPromptText("Course");

        this.courseSelect.setDropdownItems(new Lesson("Math"), new Lesson("2D Graphics"), new Lesson("OGP"),
                new Lesson("OOSD"));
        this.classRoomSelect.setDropdownItems(new Classroom(30, "Classroom 1", 0),
                new Classroom(30, "Classroom 2", 1), new Classroom(30, "Classroom 3", 2),
                new Classroom(30, "Classroom 4", 3), new Classroom(30, "Classroom 5", 4),
                new Classroom(30, "Classroom 6", 5));
        this.buildStudentGroups(new ArrayList());
        this.buildTeacherGroups(Arrays.asList(
                new Teacher(Gender.MALE, "Johan"), new Teacher(Gender.MALE, "Pieter"), new Teacher(Gender.MALE, "Edwin"),
                new Teacher(Gender.MALE, "Etienne"), new Teacher(Gender.FEMALE, "Joli"), new Teacher(Gender.FEMALE, "Jessica")));

        HBox topRowButtons = new HBox(this.startTime, this.classRoomSelect, this.teacherGroupSelect);                   //Layout
        HBox bottomRowButtons = new HBox(this.endTime, this.courseSelect, this.studentGroupSelect);
        topRowButtons.setSpacing(10);
        bottomRowButtons.setSpacing(10);

        VBox allButtons = new VBox(topRowButtons, bottomRowButtons);
        allButtons.setSpacing(10);

        //Modifications Buttons
        Button apply = new Button("Apply");                                                     //Buttons
        Button reset = new Button("Reset");
        Button remove = new Button("Remove");
        HBox applyRemoveHBox = new HBox(apply, remove);
        applyRemoveHBox.setSpacing(10);

        VBox applyRemoveResetButtons = new VBox(applyRemoveHBox, reset);                            //Layout
        applyRemoveResetButtons.setAlignment(Pos.CENTER);
        applyRemoveResetButtons.setSpacing(10);

        apply.setOnAction(event -> {
            try {
                students.clear();
                teachers.clear();
                for (int i = 0; i < studentGroupSelect.getItems().size() - 1; i++) {
                    if (((CheckMenuItem) studentGroupSelect.getItems().get(i)).isSelected())
                        students.add(new StudentGroup(String.valueOf(i + 1)));
                }
                for (int i = 0; i < teacherGroupSelect.getItems().size() - 1; i++) {
                    if (((CheckMenuItem) teacherGroupSelect.getItems().get(i)).isSelected())
                        teachers.add(new Teacher());
                }
                scheduleView.applyScheduleItem(teachers, students, classRoomSelect.getDropdownValue(),
                        Integer.parseInt(startTime.getText()), Integer.parseInt(endTime.getText()), courseSelect.getDropdownValue());
            } catch (Exception e) {
                Logger.warn(e, "Could not apply schedule item");
            }
        });

        reset.setOnAction(event -> {
            try {
                students.clear();
                scheduleView.resetSchedule();
            } catch (Exception e) {
                Logger.warn(e, "Could not reset schedule");
            }
        });

        remove.setOnAction(event -> {
            try {
                students.clear();
                for (int i = 0; i < studentGroupSelect.getItems().size() - 1; i++) {
                    CheckMenuItem temp = (CheckMenuItem) studentGroupSelect.getItems().get(i);
                    if (temp.isSelected()) {
                        students.add(new StudentGroup(String.valueOf(i + 1)));
                    }
                }
                scheduleView.removeScheduleItem(teachers, students, classRoomSelect.getDropdownValue(), Integer.parseInt(startTime.getText()), Integer.parseInt(endTime.getText()), courseSelect.getDropdownValue());
            } catch (Exception e) {
                Logger.warn(e, "Could not remove schedule item");
            }
        });

        //Save and Load
        Button save = new Button("Save");
        save.setOnAction(e -> {
            try {
                FileChooser chooser = buildFileChooser("Save schedule");
                File file = chooser.showSaveDialog(null);

                if (file != null) {
                    String json = this.scheduleView.getSchedule().toJson();
                    FileManager.write(file.getAbsolutePath(), json);
                }
            } catch (Exception ex) {
                Logger.warn(ex, "Could not save schedule");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error exporting schedule");
                alert.setHeaderText("Oh no!");
                alert.setContentText("Could not export schedule.\n\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

        Button load = new Button("Load");
        load.setOnAction(e -> {
            try {
                FileChooser chooser = buildFileChooser("Load schedule");

                File file = chooser.showOpenDialog(null);

                if (file != null && file.exists()) {
                    String json = FileManager.read(file.getAbsolutePath());
                    this.scheduleView.setSchedule(Schedule.fromJson(json));
                }
            } catch (Exception ex) {
                Logger.warn(ex, "Could not load schedule");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error importing schedule");
                alert.setHeaderText("Oh no!");
                alert.setContentText("Could not import schedule.\n\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

        VBox saveLoadButtons = new VBox(save, load);
        saveLoadButtons.setSpacing(10);

        //final
        this.buttons.getChildren().addAll(allButtons, applyRemoveResetButtons, saveLoadButtons);
        this.buttons.setSpacing(10);
        this.buttons.setPadding(new Insets(10, 10, 10, 10));
    }

    public void selectItem(ScheduleItem scheduleItem) {
        this.startTime.setText(scheduleItem.getStartPeriod() + "");
        this.endTime.setText(scheduleItem.getEndPeriod() + "");

        this.classRoomSelect.setValue(scheduleItem.getClassroom());
        this.buildTeacherGroups(scheduleItem.getTeachers());
        this.courseSelect.setValue(scheduleItem.getLesson());
        this.buildStudentGroups(scheduleItem.getStudentGroups());
    }

    private void buildStudentGroups(List<StudentGroup> selectedGroups) {
        this.studentGroupSelect.getItems().clear();
        for (int i = 1; i <= 4; i++) {
            CheckMenuItem tempItem = new CheckMenuItem(String.valueOf(i));
            for (StudentGroup selectedGroup : selectedGroups) {
                if (selectedGroup.getName().equals(String.valueOf(i)))
                    tempItem.setSelected(true);
            }
            this.studentGroupSelect.getItems().add(tempItem);
        }
    }

    private void buildTeacherGroups(List<Teacher> selectedGroups) {
        this.teacherGroupSelect.getItems().clear();
        for (Teacher teacher : selectedGroups) {
            this.teacherGroupSelect.getItems().add(new MenuItem(teacher.getName()));
        }
//        for (Teacher teacher : selectedGroups) {
//            CheckMenuItem tempItem = new CheckMenuItem(teacher.getName());
//            for (Teacher selectedGroup : selectedGroups) {
//                if (selectedGroup.getName().equals(teacher.getName())) {
//                    tempItem.setSelected(true);
//                    this.teacherGroupSelect.getItems().add(tempItem);
//                }
//            }
//        }
        for (int i = 0; i < 4; i++) {
            String name = (i + 1) + "";
            CheckMenuItem tempItem = new CheckMenuItem(name);
            for (Teacher selectedGroup : selectedGroups) {
                if (selectedGroup.getName().equals(name)) {
                    tempItem.setSelected(true);
                }
            }
            this.teacherGroupSelect.getItems().add(tempItem);
        }
    }

    private FileChooser buildFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Schedule", "*.schedule"));
        fileChooser.setInitialFileName("schedule.schedule");
        fileChooser.setInitialDirectory(new java.io.File("."));

        return fileChooser;
    }
}