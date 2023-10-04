package bdma.bigdata.project.rest;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;
import bdma.bigdata.project.mapreduce.MapReduceQ1;
import bdma.bigdata.project.mapreduce.MapReduceQ2;
import bdma.bigdata.project.mapreduce.MapReduceQ3;
import bdma.bigdata.project.mapreduce.MapReduceQ4;
import bdma.bigdata.project.mapreduce.MapReduceQ5;
import bdma.bigdata.project.mapreduce.MapReduceQ6;
import bdma.bigdata.project.mapreduce.MapReduceQ7;
import bdma.bigdata.project.rest.core.Course;
import bdma.bigdata.project.rest.core.Student;
import bdma.bigdata.project.rest.core.responseCourse;
import bdma.bigdata.project.rest.core.responseInstructor;

@Path("/asbu")
public class asbu {

    @GET
    @Path("/students/{id}/transcripts/{program}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getStudents(@PathParam("id") String numEt, @PathParam("program") String semesters) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ1");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ1"));
        }
        MapReduceQ1.main(new String[]{numEt, semesters});
        Map<String, String> first = null;
        Map<String, String> second = null;
        File result1 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ1/part-r-00000");
        Scanner reader = new Scanner(result1);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            String key = keyValue[0];
            String note = keyValue[1];
            String annee = key.split("/")[0];
            String id = key.split("/")[1];
            Course a = new Course(id, annee);
            if (first == null) {
                first = new TreeMap<>();
            }
            if (second == null) {
                second = new TreeMap<>();
            }
            if (Integer.valueOf(id.substring(1, 3)) % 2 == 0) {
                second.put(a.getNom(), note);
            } else {
                first.put(a.getNom(), note);
            }
        }
        reader.close();
        Student s1 = new Student(numEt, semesters);
        s1.setProgram(semesters);
        s1.setFirst(first);
        s1.setSecond(second);
        Map<String, Object> result = new TreeMap<String, Object>();
        result.put("Name", s1.getName());
        result.put("Email", s1.getMail());
        result.put("Program", s1.getProgram());
        result.put("First", first);
        result.put("Second", second);
        if (s1.getName() != null) {
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found: " + numEt).build();
        }
    }

    @GET
    @Path("/rates/{semester}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getRatesSem(@PathParam("semester") String semester) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ2");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ2Temp"));
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ2"));
        }
        MapReduceQ2.main(new String[]{semester});
        Map<String, String> ratesYear = new TreeMap<String, String>();
        File result2 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ2/part-r-00000");
        Scanner reader = new Scanner(result2);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            ratesYear.put(keyValue[0], keyValue[1]);
        }
        reader.close();
        if (ratesYear.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(ratesYear, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("courses/{id}/rates")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getRatesCourse(@PathParam("id") String id) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ3");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ3"));
        }
        MapReduceQ3.main(new String[]{id});
        Map<String, String> ratesCourse = new TreeMap<String, String>();
        File result3 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ3/part-r-00000");
        Scanner reader = new Scanner(result3);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            ratesCourse.put(keyValue[0], keyValue[1]);
        }
        reader.close();
        if (ratesCourse.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(ratesCourse, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("courses/{id}/rates/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getRatesCourseYear(@PathParam("id") String id, @PathParam("year") String year) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ4");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ4"));
        }
        MapReduceQ4.main(new String[]{id, year});
        Map<String, String> ratesCourseYear = new TreeMap<String, String>();
        File result3 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ4/part-r-00000");
        Scanner reader = new Scanner(result3);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            ratesCourseYear.put(keyValue[0], keyValue[1]);
        }
        reader.close();
        if (ratesCourseYear.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(ratesCourseYear, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/programs/{program}/means/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getRatesSem(@PathParam("program") String program, @PathParam("year") String year) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ5");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ5"));
        }
        MapReduceQ5.main(new String[]{program, year});
        Map<String, responseCourse> programYear = new TreeMap<String, responseCourse>();
        File result2 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ5/part-r-00000");
        Scanner reader = new Scanner(result2);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            String[] keyParts = keyValue[0].split("/");
            responseCourse rc = new responseCourse(keyParts[0], keyValue[1]);
            programYear.put(keyParts[1], rc);
        }
        reader.close();
        if (programYear.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(programYear, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/instructors/{name}/rates")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getInstructor(@PathParam("name") String name) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ6");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ6"));
        }
        MapReduceQ6.main(new String[]{name});
        Map<String, responseInstructor> InstructorCourses = new TreeMap<String, responseInstructor>();
        File result6 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ6/part-r-00000");
        Scanner reader = new Scanner(result6);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            String[] keyValues = keyValue[0].split("/");
            String rate = keyValue[1];
            String year = keyValues[0];
            String CourseId = keyValues[1];
            String nameC = keyValues[2];
            String resp = CourseId + "/" + year;
            responseInstructor r = new responseInstructor(nameC, rate);
            InstructorCourses.put(resp, r);
        }
        reader.close();
        if (InstructorCourses.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(InstructorCourses, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/ranks/{program}/years/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getInstructor(@PathParam("program") String program, @PathParam("year") String year) throws Exception {
        File f = new File("/home/hadoop/Workspace/RestServer/Results/resuQ7");
        if (f.exists() && f.isDirectory()) {
            FileUtils.forceDelete(new File("/home/hadoop/Workspace/RestServer/Results/resuQ7"));
        }
        MapReduceQ7.main(new String[]{program, year});
        HashMap<String, Float> notes = new HashMap<>();
        Map<Object, Object> rankOrder = null;
        File result2 = new File("/home/hadoop/Workspace/RestServer/Results/resuQ7/part-r-00000");
        Scanner reader = new Scanner(result2);
        while (reader.hasNextLine()) {
            String[] keyValue = reader.nextLine().split("\t");
            notes.put(" " + keyValue[0], Float.valueOf(keyValue[1]));
            rankOrder =
                    notes.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        }
        if (rankOrder.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } else {
            return Response.ok(rankOrder, MediaType.APPLICATION_JSON).build();
        }
    }
}