package com.example.cookbook_k12_it3_nhom2.models;

public class Step {
    private int step_number;
    private String instruction;

    public Step() {}

    public Step(int step_number, String instruction) {
        this.step_number = step_number;
        this.instruction = instruction;
    }

    public int getStep_number() {
        return step_number;
    }

    public void setStep_number(int step_number) {
        this.step_number = step_number;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
