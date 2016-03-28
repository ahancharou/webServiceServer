package training;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Path("/user")
public class Receiver {

    @GET
    @Produces("application/json")
    public List<User> getUsers () {
            return InfoBase.getUsers();
    }

    @GET
    @Path("{login}")
    @Produces("application/json")
    public User getUserByLogin(@PathParam("login") String login) {
        User user = InfoBase.findUserByLogin(login);
        if (user == null) {
            throw new RuntimeException("Can't find user with login: " + login);
        }
        return user;
    }

    @PUT
    @Consumes("application/xml")
    public void addUser(JAXBElement<User> user) {
        if (!InfoBase.addUser(user.getValue())) {
            throw new RuntimeException("Can't add user with login: "
                    + user.getValue().getLogin());
        }
    }

    @DELETE
    @Path("{login}")
    public void deleteUser(@PathParam("login") String login) {
        if (!InfoBase.deleteUserByLogin(login)) {
            throw new RuntimeException("Can't delete user with login: " + login);
        }
    }

    @POST
    @Consumes("application/json")
    public void updateUser(JAXBElement<User> user) {
        if (!InfoBase.updateUser(user.getValue())) {
            throw new RuntimeException("Can't update user with login: "
                    + user.getValue().getLogin());
        }
    }

    static final String FOLDER = "d:/WebService/JPG/";

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPic(FormDataMultiPart formDataMultiPart
            ){
        ContentDisposition contentDisposition = formDataMultiPart.getContentDisposition();
        FormDataBodyPart filePart = formDataMultiPart.getField("file");

        String filePath = FOLDER+ contentDisposition + contentDisposition.getFileName();
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);

        try {
            OutputStream outputStream = new FileOutputStream(new File(filePath));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0 , read);
            }

            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String output = "Pic saved to "+ filePath;
        return Response.status(200).entity(output).build();
    }

}
