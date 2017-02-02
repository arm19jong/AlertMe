package com.jongzazaal.alertme;

import java.util.ArrayList;

/**
 * Created by jongzazaal on 4/1/2560.
 */

public class SwitchService {

    private ArrayList<String> serviceName = new ArrayList<>();
    private static SwitchService switchService;
    private SwitchService(){}
    public static SwitchService getInstace(){
        if(switchService == null){
            switchService = new SwitchService();
        }
        return switchService;
    }

    public ArrayList<String> getServiceName(){
        return serviceName;
    }

    public void addServiceName(String serviceName){
        this.serviceName.add(serviceName);
    }

    public void removeServiceName(String serviceName){
        this.serviceName.remove(serviceName);
    }


    public boolean isOnService(String serviceName){
        boolean on = false;
        for (String s: this.serviceName) {
            if(s.equals(serviceName)){
                on = true;
                break;
            }
        }
        return on;
    }

}
