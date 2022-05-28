package com.javademo.designpattern.behavioral;

public class InterpreterPattern {
    //解释器模式：
    //解释器模式可以为语言创建解释器，通过该解释器来确定该句子的语法是否成立，该模式可利用场景较少
    //例子：句子里面如果包含 红色 和 model3就表示成立

    public abstract static class Expression{
        public abstract boolean interpret(String str);
    }

    //终端解释器
    public static class TerminalExpression extends Expression{

        private String word;

        public TerminalExpression(String word){
            this.word = word;
        }

        @Override
        public boolean interpret(String str) {
            return str.contains(this.word);
        }
    }

    //解释器并且组合
    public static class AndTerminalExpression extends Expression{

        private Expression expression1;
        private Expression expression2;

        public AndTerminalExpression(Expression expression1,Expression expression2){
            this.expression1 = expression1;
            this.expression2 = expression2;
        }

        @Override
        public boolean interpret(String str) {
            return this.expression1.interpret(str) && this.expression2.interpret(str);
        }
    }

    public static void main(String[] args) {

        Expression expression1 = new TerminalExpression("红色");
        Expression expression2 = new TerminalExpression("model3");
        Expression expression = new AndTerminalExpression(expression1, expression2);
        if (expression.interpret("这是一辆红色model3")){
            System.out.println("这是一辆红色model3 true");
        }
    }
}
