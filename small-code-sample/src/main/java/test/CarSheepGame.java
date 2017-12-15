package test;

import java.util.Random;

public class CarSheepGame {

    private static final int DOOR_NUM = 3;
    private static final int TIME = 10000;

    public static void main(String[] args) {
        Host host = new Host();
        Door[] doors = host.createDoors(DOOR_NUM);
        
        Player player = new Player();
        Door firstSelect = player.firstSelect(doors);
        
        Door remained = host.remainedDoor(doors, firstSelect);
        
        Door finalSelect = null;

        int changeMatched = 0;
        int notChangeMatched = 0;
        for (int i = 0; i < TIME; i++) {
            changeMatched = oneRound(true) ? changeMatched + 1 : changeMatched;;
            notChangeMatched = oneRound(false) ? notChangeMatched + 1 : notChangeMatched;;
        }
        
        System.out.println("time: " + TIME);
        System.out.println("change select matched time: " + changeMatched);
        System.out.println("not change select matched time: " + notChangeMatched);
    }
    
    public static boolean oneRound(boolean change) {
        Host host = new Host();
        Door[] doors = host.createDoors(DOOR_NUM);
        
        Player player = new Player();
        Door firstSelect = player.firstSelect(doors);
        
        Door remained = host.remainedDoor(doors, firstSelect);
        
        return change ? host.isMatched(remained) : host.isMatched(firstSelect);
    }

}

class Host {
    private int carIndex;
    
    public Door[] createDoors(int doorNum) {
        Random random = new Random();
        this.carIndex = Math.abs(random.nextInt()) % doorNum;
        Door[] doors = new Door[doorNum];
        String[] types = new String[doorNum];
        
        for (int i = 0; i < doors.length; i++) {
            if (i == carIndex) {
                Door door = new Door("car", i);
                doors[i] = door;
                types[i] = "car";
            }
            else {
                Door door = new Door("sheep", i);
                doors[i] = door;
                types[i] = "sheep";
            }
        }

        return doors;
    }

    public Door remainedDoor(Door[] doors, Door firstSelect) {

        Random random = new Random();
        int remained = Math.abs(random.nextInt()) % doors.length;
        
        if (firstSelect.getNum() == this.carIndex) {
            while (remained == this.carIndex) {
                remained = Math.abs(random.nextInt()) % doors.length;
            }
        }
        else {
            remained = this.carIndex;
        }
        
        return doors[remained];
    }
    
    public boolean isMatched(Door secondSelect) {
        return secondSelect.getNum() == this.carIndex;
    }
}

class Player {
    public Door firstSelect(Door[] doors) {
        Random random = new Random();
        int select = Math.abs(random.nextInt()) % doors.length;
        
        return doors[select];
    }
}

class Door {
    private String type;
    private int num;
    
    public Door() {}
    
    public Door(String type, int num) {
        this.type = type;
        this.num = num;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }
}