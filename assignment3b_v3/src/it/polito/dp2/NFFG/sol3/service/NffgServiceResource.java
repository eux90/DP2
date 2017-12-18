package it.polito.dp2.NFFG.sol3.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/resource")
@Api(value = "/resource", description = "the collection of service resources")
public class NffgServiceResource {
	// create an instance of the object that can execute operations

	private final static String Error400 = "HTTP 400 - Bad Request ";
	private final static String Error403 = "HTTP 403 - Forbidden ";
	private final static String Error404 = "HTTP 404 - Not Found ";
	private final static String Error503 = "HTTP 503 - Service Unavailable ";
	private static Logger logger = Logger.getLogger(NffgServiceResource.class.getName());
	private NffgService service;
	private String responseBodyTemplate;

	public NffgServiceResource() {
		try {
			service = new NffgService();
			InputStream templateStream = NffgServiceResource.class.getResourceAsStream("/html/GeneralBodyTemplate.html");
			if (templateStream == null) {
				logger.log(Level.SEVERE, "html General template file Not found.");
				throw new IOException();
			}
	        BufferedReader reader = new BufferedReader(new InputStreamReader(templateStream));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
			responseBodyTemplate = out.toString();
			templateStream.close();
		} catch (DatatypeConfigurationException | NullPointerException | IOException e) {
			logger.log(Level.SEVERE, "NFFG Service is unable to start please check the server configuration", e);
		}
	}

	@POST
	@Path("/nffgs")
	@ApiOperation(value = "Add a single NFFG to NffgService Database and to Neo4J Database", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request because submitted data validation failed"),
			@ApiResponse(code = 403, message = "Forbidden because data violates some database constraint"),
			@ApiResponse(code = 503, message = "Neo4j Service not available."),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public Response postNffg(NffgType nffg, @Context UriInfo uriInfo) {

		NffgType created;
		try {
			created = service.loadNffg(nffg);
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			URI u = builder.path(created.getName()).build();
			return Response.created(u).entity(created).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));
		} catch (MyForbiddenException e) {
			throw new ForbiddenException(makeForbidden(Error403, "Forbidden", e.getLocalizedMessage()));
		} catch (Neo4jServiceException e) {
			throw new ServiceUnavailableException(makeServiceUnavailable(Error503, "Service Unavailable", "Neo4J Service Not Available"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during POST of an NFFG", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/policies")
	@ApiOperation(value = "Add a single Policy to NffgService Database", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request because submitted data validation failed"),
			@ApiResponse(code = 403, message = "Forbidden because data violates some database constraint"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public Response postPolicy(PolicyType policy, @Context UriInfo uriInfo) {

		try {
			PolicyType created = service.loadPolicy(policy);
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			String policyName;

			if (created.getRchPolicy() != null) {
				policyName = created.getRchPolicy().getName();
			} else {
				policyName = created.getTrvPolicy().getName();
			}
			URI u = builder.path(policyName).build();
			return Response.created(u).entity(created).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));		
		} catch (MyForbiddenException e) {
			throw new ForbiddenException(makeForbidden(Error403, "Forbidden", e.getLocalizedMessage()));	
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during POST of a Policy", e);
			return Response.serverError().build();
		}

	}

	@PUT
	@Path("/policies")
	@ApiOperation(value = "Update an existing Policy", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok, policy updated"),
			@ApiResponse(code = 400, message = "Bad Request because submitted data validation failed"),
			@ApiResponse(code = 403, message = "Forbidden because data violates some database constraint"),
			@ApiResponse(code = 404, message = "Not Found. Specified Policy is not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public Response putPolicy(PolicyType policy, @Context UriInfo uriInfo) {

		try {
			PolicyType updated = service.modifyPolicy(policy);
			return Response.ok(updated).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));
		} catch (MyForbiddenException e) {
			throw new ForbiddenException(makeForbidden(Error403, "Forbidden", e.getLocalizedMessage()));
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during update (PUT) of a Policy", e);
			return Response.serverError().build();
		}

	}
	
	@GET
	@Path("/database")
	@ApiOperation(value = "Get the whole database", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({ MediaType.APPLICATION_XML })
	public Response getDatabase() {
		try {
			NffgInfoType nffgInfo = service.getDatabase();
			return Response.ok(nffgInfo).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of database", e);
			return Response.serverError().build();
		}
	}
	
	
	@GET
	@Path("/nffgs")
	@ApiOperation(value = "Get the list of NFFGs", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request because query parameters are invalid."),
			@ApiResponse(code = 404, message = "Not Found. Some of the specified NFFGs are not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getNffgs(@DefaultValue("false") @QueryParam("all") boolean all,
			@QueryParam("id") List<String> names) {
		try {
			NffgSetType nffgs = service.getNffgs(all, names);
			return Response.ok(nffgs).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of NFFGs Set", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/policies")
	@ApiOperation(value = "Get the list of policies", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request because parameters are invalid or because some Policies violates Database constraints."),
			@ApiResponse(code = 404, message = "Not Found. Some of the specified Polices are not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getPolicies(@DefaultValue("false") @QueryParam("all") boolean all,
			@QueryParam("id") List<String> names) {
		try {
			PolicySetType policies = service.getPolicies(all, names);
			return Response.ok(policies).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));	
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of Policies Set", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/verifications")
	@ApiOperation(value = "Get the list of verified Policies", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request because query parameters are invalid."),
			@ApiResponse(code = 404, message = "Not Found. Some of the specified Polices are not in NffgService Database"),
			@ApiResponse(code = 503, message = "Neo4j Service not available."),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getVerifications(@DefaultValue("false") @QueryParam("all") boolean all,
			@QueryParam("id") List<String> names) {
		try {
			PolicySetType VerifiedPolicies = service.getVerifications(all, names);
			return Response.ok(VerifiedPolicies).build();
		} catch (MyBadRequestException e) {
			throw new BadRequestException(makeBadRequest(Error400, "Bad Request", e.getLocalizedMessage()));
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Neo4jServiceException e) {
			throw new ServiceUnavailableException(makeServiceUnavailable(Error503, "Service Unavailable", "Neo4J Service Not Available"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of Verified Policies Set", e);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("/verifications/{id}")
	@ApiOperation(value = "Get a verified Policy", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Not Found. Specified Policy is not in NffgService Database"),
			@ApiResponse(code = 503, message = "Neo4j Service not available."),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getVerification(@PathParam("id") String id) {
		try {
			PolicyType VerifiedPolicy = service.getVerification(id);
			return Response.ok(VerifiedPolicy).build();
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Neo4jServiceException e) {
			throw new ServiceUnavailableException(makeServiceUnavailable(Error503, "Service Unavailable", "Neo4J Service Not Available"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of a Verified Policy", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/nffgs/{id}")
	@ApiOperation(value = "Get an NFFG", notes = "xml formats")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Not Found. Specified NFFG is not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getNffg(@PathParam("id") String id) {

		NffgType got;
		try {
			got = service.getNffg(id);
			return Response.ok(got).build();
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of an NFFG", e);
			return Response.serverError().build();
		}

	}

	@GET
	@Path("/policies/{id}")
	@ApiOperation(value = "Get a policy", notes = "xml formats")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Not Found. Specified Policy is not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getPolicy(@PathParam("id") String id) {

		PolicyType got;
		try {
			got = service.getPolicy(id);
			return Response.ok(got).build();
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during GET of a Policy", e);
			return Response.serverError().build();
		}

	}

	@DELETE
	@Path("/policies/{id}")
	@ApiOperation(value = "Remove a policy", notes = "xml formats")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Not Found. Specified Policy is not in NffgService Database"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response deletePolicy(@PathParam("id") String id) {

		PolicyType deleted;
		try {
			deleted = service.removePolicy(id);
			return Response.ok(deleted).build();
		} catch (MyNotFoundException e) {
			throw new NotFoundException(makeNotFound(Error404, "Not Found", e.getLocalizedMessage()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Server error occurred during DELETE of a Policy", e);
			return Response.serverError().build();
		}
	}
	
	// to be implemented (now used for artifacts generation)
	@POST
	@Path("/database")
	@ApiOperation(value = "Add a whole database", notes = "xml format")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found")})
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public Response loadDatabase(NffgInfoType nffgInfo) {
		//TODO
		return Response.status(Status.METHOD_NOT_ALLOWED).build();
	}
	
	private Response makeBadRequest(String myTitle, String myType, String myMessage){
			String responseBody = new String(responseBodyTemplate);
			String message = responseBody
					.replaceFirst("___TO_BE_REPLACED___", myMessage)
					.replaceFirst("___TITLE_TO_BE_REPLACED___", myTitle)
					.replaceFirst("___TYPE_TO_BE_REPLACED___", myType);
			return Response.status(Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_HTML).build();
	}
	
	private Response makeNotFound(String myTitle, String myType, String myMessage){
		String responseBody = new String(responseBodyTemplate);
		String message = responseBody
				.replaceFirst("___TO_BE_REPLACED___", myMessage)
				.replaceFirst("___TITLE_TO_BE_REPLACED___", myTitle)
				.replaceFirst("___TYPE_TO_BE_REPLACED___", myType);
		return Response.status(Status.NOT_FOUND).entity(message).type(MediaType.TEXT_HTML).build();
	}
	private Response makeForbidden(String myTitle, String myType, String myMessage){
		String responseBody = new String(responseBodyTemplate);
		String message = responseBody
				.replaceFirst("___TO_BE_REPLACED___", myMessage)
				.replaceFirst("___TITLE_TO_BE_REPLACED___", myTitle)
				.replaceFirst("___TYPE_TO_BE_REPLACED___", myType);
		return Response.status(Status.FORBIDDEN).entity(message).type(MediaType.TEXT_HTML).build();
	}
	private Response makeServiceUnavailable(String myTitle, String myType, String myMessage){
		String responseBody = new String(responseBodyTemplate);
		String message = responseBody
				.replaceFirst("___TO_BE_REPLACED___", myMessage)
				.replaceFirst("___TITLE_TO_BE_REPLACED___", myTitle)
				.replaceFirst("___TYPE_TO_BE_REPLACED___", myType);
		return Response.status(Status.SERVICE_UNAVAILABLE).entity(message).type(MediaType.TEXT_HTML).build();
	}
}
