package com.javademo.designpattern.j2ee;

public class CompositeEntityPattern {
    //组合实体模式：
    //组合实体模式表示将多个实体组合成一个实体，通过更新组合实体自动更新所依赖的实体
    //例子：全车清洁包括内饰清洗和外部清洗，用组合实体模式表达

    //组合实体
    public static class CompositeEntiy{

        private InteriorClean interiorClean;
        private OutsideClean outsideClean;

        public CompositeEntiy(){
            this.interiorClean = new InteriorClean(false);
            this.outsideClean = new OutsideClean(false);
        }

        public void updateClean(boolean interiorState, boolean outsideState){
            this.interiorClean.setClean(interiorState);
            this.outsideClean.setClean(outsideState);
        }

        public void getCleanState(){
            this.interiorClean.info();
            this.outsideClean.info();
        }
    }

    //内饰清洗
    public static class InteriorClean{

        private boolean isClean;

        public InteriorClean(boolean isClean) {
            this.isClean = isClean;
        }

        public void setClean(boolean clean) {
            isClean = clean;
        }

        public boolean getClean(){
            return isClean;
        }

        public void info(){
            System.out.println(isClean ? "内饰已清洗" : "内饰未清洗");
        }
    }

    //外部清洗
    public static class OutsideClean{

        private boolean isClean;

        public OutsideClean(boolean isClean) {
            this.isClean = isClean;
        }

        public void setClean(boolean clean) {
            isClean = clean;
        }

        public boolean getClean(){
            return isClean;
        }

        public void info(){
            System.out.println(isClean ? "外部已清洗" : "外部未清洗");
        }
    }

    public static void main(String[] args) {
        CompositeEntiy compositeEntiy = new CompositeEntiy();
        compositeEntiy.updateClean(true, true);
        compositeEntiy.getCleanState();
    }
}
