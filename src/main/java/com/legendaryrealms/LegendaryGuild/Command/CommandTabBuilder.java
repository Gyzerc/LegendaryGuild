package com.legendaryrealms.LegendaryGuild.Command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandTabBuilder {

    private Set<TabList> list;
    public CommandTabBuilder(){
        list = new HashSet<>();
    }

    public CommandTabBuilder addTab(List<String> returnList,int position,List<String> previousArg,int previousPosition){
        list.add(new TabList(returnList,position,previousArg,previousPosition));
        return this;
    }

    public List<String> build(String[] args){
        List<String> returnList=new ArrayList<>();
        int length = args.length;
        if (length > 0) {
            for (TabList tabList : list) {
                if (tabList.getPosition() == length-1) {
                    if (tabList.getPreviousPosition() >= length) {
                        continue;
                    }
                    if ( tabList.getPreviousArg() == null){
                        continue;
                    }
                    String previousArg = args[tabList.getPreviousPosition()];

                    if (tabList.getPreviousArg().contains(previousArg)){
                        returnList = tabList.getReturnList();
                        break;
                    }
                }
            }
        }
        return returnList;
    }

    public class TabList {

        private List<String> returnList;
        //此参数出现的位置
        private int position;

        //识别上一个参数
        private List<String> previousArg;
        //上一个参数出现的位置
        private int previousPosition;

        public TabList(List<String> returnList, int position, List<String> previousArg, int previousPosition) {
            this.returnList = returnList;
            this.position = position;
            this.previousArg = previousArg;
            this.previousPosition = previousPosition;
        }

        public List<String> getReturnList() {
            return returnList;
        }

        public int getPosition() {
            return position;
        }

        public List<String> getPreviousArg() {
            return previousArg;
        }

        public int getPreviousPosition() {
            return previousPosition;
        }
    }

}
