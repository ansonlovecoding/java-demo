package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class CommandPattern {
    //命令模式：
    //命令模式是将一个请求/命令封装成一个对象，这样可以对不同请求来参数化其他对象，使其具备执行多项命令的能力
    //例子：车机系统有一键开启舒适模式的按钮，开启舒适模式会自动开启空调和打开音乐播放，用命令模式来表达

    //命令接口
    public interface Command{
        void execute();
    }

    //空调类
    public static class Aircon{
        public void on(){
            System.out.println("空调开启！");
        }

        public void off(){
            System.out.println("空调关闭！");
        }
    }

    //音乐类
    public static class Music{
        public void on(){
            System.out.println("音乐播放！");
        }

        public void off(){
            System.out.println("音乐停止！");
        }
    }

    //空调开启命令
    public static class AirconOnCommand implements Command{

        private Aircon aircon;

        public AirconOnCommand(Aircon aircon){
            this.aircon = aircon;
        }

        @Override
        public void execute() {
            this.aircon.on();
        }
    }

    //空调关闭命令
    public static class AirconOffCommand implements Command{

        private Aircon aircon;

        public AirconOffCommand(Aircon aircon){
            this.aircon = aircon;
        }

        @Override
        public void execute() {
            this.aircon.off();
        }
    }

    //音乐播放命令
    public static class MusicOnCommand implements Command{

        private Music music;

        public MusicOnCommand(Music music){
            this.music = music;
        }

        @Override
        public void execute() {
            this.music.on();
        }
    }

    //音乐停止命令
    public static class MusicOffCommand implements Command{

        private Music music;

        public MusicOffCommand(Music music){
            this.music = music;
        }

        @Override
        public void execute() {
            this.music.off();
        }
    }

    //车机系统
    public static class VehicleSystem{

        //开启舒适模式命令
        private List<Command> openComfortCommonds;
        //关闭舒适模式命令
        private List<Command> closeComfortCommonds;

        public VehicleSystem(){
            this.openComfortCommonds = new ArrayList<>(2);
            this.closeComfortCommonds = new ArrayList<>(2);
        }

        //添加打开舒适模式命令
        public void addOpenComfortCommand(Command command){
            this.openComfortCommonds.add(command);
        }

        //添加关闭舒适模式命令
        public void addCloseComfortCommand(Command command){
            this.closeComfortCommonds.add(command);
        }

        //执行舒适模式
        public void openComfort(){
            System.out.println("开启舒适模式！");
            for (Command command:this.openComfortCommonds
                 ) {
                command.execute();
            }
        }

        //关闭舒适模式
        public void closeComfort(){
            System.out.println("关闭舒适模式！");
            for (Command command:this.closeComfortCommonds
            ) {
                command.execute();
            }
        }
    }

    public static void main(String[] args) {
        Aircon aircon = new Aircon();
        Music music = new Music();
        Command airconOnCommand = new AirconOnCommand(aircon);
        Command airconOffCommand = new AirconOffCommand(aircon);
        Command musicOnCommand = new MusicOnCommand(music);
        Command musicOffCommand = new MusicOffCommand(music);

        VehicleSystem vehicleSystem = new VehicleSystem();
        vehicleSystem.addOpenComfortCommand(airconOnCommand);
        vehicleSystem.addOpenComfortCommand(musicOnCommand);
        vehicleSystem.addCloseComfortCommand(airconOffCommand);
        vehicleSystem.addCloseComfortCommand(musicOffCommand);

        vehicleSystem.openComfort();
        vehicleSystem.closeComfort();

    }

}
