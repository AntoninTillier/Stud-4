package bdma.bigdata.project.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

@Path("/TableService")

public class TableService {

    @GET
    @Path("/tables")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTables() {
        List<String> tableList = new ArrayList<>();
        try {
            Configuration config = HBaseConfiguration.create();
            Connection connection = ConnectionFactory.createConnection(config);
            Admin admin = connection.getAdmin();
            HTableDescriptor[] tables = admin.listTables();
            for (HTableDescriptor table : tables) {
                tableList.add(table.getTableName().getNameAsString());
            }
        } catch (Exception e) {
            return Response.ok("Error", MediaType.APPLICATION_JSON).entity("Table error.").build();
        }
        if (tableList.isEmpty()) {
            return Response.ok("Not Found", MediaType.APPLICATION_JSON).entity("Table not found.").build();
        } else {
            return Response.ok(tableList, MediaType.APPLICATION_JSON).build();
        }
    }
}