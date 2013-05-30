package divi.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import divi.Activator;

public class DiviConfiguration {
    public static final String[] DEFAULT_INJECT_ANNOTATION_ARRAY = new String[] { "Inject", "EJB", "Autowired" };
    public static final String P_ANNOTATIONS = "annotations";

    public static String createAnnotationPreference(String[] annotationArray) {
        String result = "";
        for (String annotation : annotationArray) {
            if (result.equals(""))
                result = annotation;
            else {
                result = result + ";" + annotation;
            }
        }
        return result;
    }

    public static String[] parseAnnotationPreference(String preferenceValue) {
        return preferenceValue.split(";");
    }

    public static Set<String> getInjectAnnotationSet() {
        return new HashSet<String>(Arrays.asList(parseAnnotationPreference(Activator.getDefault().getPreferenceStore().getString(P_ANNOTATIONS))));
    }
}
