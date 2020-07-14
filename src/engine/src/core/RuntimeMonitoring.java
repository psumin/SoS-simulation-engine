package core;

import java.lang.reflect.Constructor;
import java.lang.reflect.*;

public class RuntimeMonitoring {

    public void classLoader(World world, String className, double property) {


        try {
            Class<?> cls = Class.forName(className);
//            System.out.println("Loaded class : " + cls);
            Method method = cls.getDeclaredMethod("getRescuedRate", null);
//            Method method = cls.getDeclaredMethod("RescuedRate", int.class, int.class);
//            System.out.println("Got method: " + method);

//            System.out.println("Before");
            float patientRescuedRate = (float)method.invoke(world, null);

            System.out.println("Patient Rescued Rate : " + patientRescuedRate);

            if(patientRescuedRate < (float)property)
                System.out.println("Verification result: False");
            else
                System.out.println("Verification result: True");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

//    rescuedPatientCount / (float)maxPatient;

}
