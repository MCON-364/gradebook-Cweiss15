package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        if (gradesByStudent.containsKey(name)) {
            return false;
        }
        gradesByStudent.put(name, new ArrayList<>());
        activityLog.add(name + " was added to your grade book.");
        return true;
    }

    public boolean addGrade(String name, int grade) {
        if(gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).add(grade);
            activityLog.add("Grade of " + grade + " was added for " + name);
            UndoAction undoAction = gradebook -> {List<Integer> grades = gradesByStudent.get(name);
            grades.remove(grades.size()-1);};
            undoStack.push(undoAction);
            return true;
        }
        return false;

    }

    public boolean removeStudent(String name) {
        if (gradesByStudent.containsKey(name)) {
            List<Integer> grades = gradesByStudent.get(name);
            gradesByStudent.remove(name);
            activityLog.add(name + " was removed from your gradebook");
            UndoAction undoAction = gradebook -> {
                gradesByStudent.put(name, new ArrayList<>());
                if(!grades.isEmpty()) {
                    for(Integer grade : grades) {
                        gradesByStudent.get(name).add(grade);
                    }
                }
            };
            undoStack.push(undoAction);
            return true;
        }
        return false;
    }

    public Optional<Double> averageFor(String name) {
        if (gradesByStudent.containsKey(name) && !gradesByStudent.get(name).isEmpty()) {
            double total = 0;
            int cnt = 0;
            for (Integer grade : gradesByStudent.get(name)) {
                total += grade;
                cnt++;
            }
            activityLog.add("An average of " + total / cnt + " was calculated for " + name);
            return Optional.of(total / cnt);
        }
        return Optional.empty();
    }

    public Optional<String> letterGradeFor(String name) {
        Optional<Double> average = averageFor(name);
        if (average.isPresent()) {
            double grade = average.get();
            String letter = switch ((int) grade / 10) {
                case 8 -> {
                    yield "B";
                }
                case 7 -> {
                    yield "C";
                }
                case 6 -> {
                    yield "D";
                }
                case 5, 4, 3, 2, 1, 0 -> {
                    yield "F";
                }
                default -> {
                    yield "A";
                }
            };
            activityLog.add("A letter grade of " + letter + " was calculated for " + name);
            return Optional.of(letter);
        }
        return Optional.empty();
    }

    public Optional<Double> classAverage() {
        double total = 0;
        int cnt = 0;
        if (gradesByStudent.isEmpty()) {
            return Optional.empty();
        }
        for (String name: gradesByStudent.keySet()) {
            Optional<Double> average = averageFor(name);
            if (average.isPresent()) {
                total+=average.get();
                cnt++;
            }
        }
        if (total==0) return Optional.empty();
        activityLog.add("A class average of " + total / cnt + " was calculated");
        return Optional.of(total/cnt);
    }

    public boolean undo() {
        if(undoStack.isEmpty()) return false;
        UndoAction undoAction = undoStack.pop();
        undoAction.undo(this);
        return true;
    }

    public List<String> recentLog(int maxItems) {
        List<String> log = new ArrayList<>();
        int size = activityLog.size();
        for (int i = size-1; log.size() < maxItems && i>=0; i--) {
            log.add(activityLog.get(i));
        }
        return log;
    }
}
