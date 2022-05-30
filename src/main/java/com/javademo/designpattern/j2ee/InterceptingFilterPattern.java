package com.javademo.designpattern.j2ee;

import java.util.ArrayList;
import java.util.List;

public class InterceptingFilterPattern {
    //拦截过滤器模式：
    //拦截过滤器模式用于对应用程序的请求或响应做一些预处理/后处理。
    //例子：创建过滤链，进行一组过滤操作

    //过滤接口
    public interface Filter{
        void execute(String request);
    }

    //权限过滤
    public static class AuthFilter implements Filter{

        @Override
        public void execute(String request) {
            System.out.println("权限判断...");
        }
    }

    //类型过滤
    public static class TypeFilter implements Filter{

        @Override
        public void execute(String request) {
            System.out.println("类型判断...");
        }
    }

    //过滤链
    public static class FilterChain {

        private List<Filter> filters = new ArrayList<>();

        public void addFilter(Filter filter){
            filters.add(filter);
        }

        public void execute(String request){
            for (Filter filter:filters
                 ) {
                filter.execute(request);
            }
            System.out.println("过滤链校验完成！");
        }
    }

    //过滤管理器
    public static class FilterManager{
        private FilterChain filterChain;

        public FilterManager(){
            this.filterChain = new FilterChain();
        }

        public void addFilter(Filter filter){
            filterChain.addFilter(filter);
        }

        public void execute(String request){
            filterChain.execute(request);
        }
    }

    //客户端
    public static class Client{
        private FilterManager filterManager;

        public Client(FilterManager filterManager){
            this.filterManager = filterManager;
        }

        public void sendRequest(String request){
            filterManager.execute(request);
        }
    }

    public static void main(String[] args) {
        FilterManager filterManager = new FilterManager();
        filterManager.addFilter(new AuthFilter());
        filterManager.addFilter(new TypeFilter());

        Client client = new Client(filterManager);
        client.sendRequest("test");
    }
}
