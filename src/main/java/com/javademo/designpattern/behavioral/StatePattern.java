package com.javademo.designpattern.behavioral;

public class StatePattern {
    //状态模式：
    //状态模式允许对象在内部状态改变时改变他的行为，对象看起来好像修改了它所属的类
    //车子的状态有通点，点火，熄火三种状态，在状态切换时输出当前状态，用状态模式来表达

    //状态接口
    public interface State{
        void doAction(Vehicle vehicle);
    }

    //汽车类
    public static class Vehicle{
        //状态
        private State state;

        public Vehicle(){
            this.state = null;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
    }

    //通电状态
    public static class ElectrifyState implements State{

        @Override
        public void doAction(Vehicle vehicle) {
            vehicle.setState(this);
            System.out.println("已通电！");
        }
    }

    //点火状态
    public static class EngineStartState implements State{
        @Override
        public void doAction(Vehicle vehicle) {
            vehicle.setState(this);
            System.out.println("已点火！");
        }
    }

    //熄火状态
    public static class offState implements State{
        @Override
        public void doAction(Vehicle vehicle) {
            vehicle.setState(this);
            System.out.println("已熄火！");
        }
    }

    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle();

        State electrifyState = new ElectrifyState();
        electrifyState.doAction(vehicle);
        State engineStartState = new EngineStartState();
        engineStartState.doAction(vehicle);
        State offState = new offState();
        offState.doAction(vehicle);

    }
}
