package si.luka2.prpo.sportapp.api.v1.resources;

import si.luka2.prpo.sportapp.beans.CommentBean;
import si.luka2.prpo.sportapp.entities.Comment;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

@ApplicationScoped
@Path("comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    private CommentBean commentBean;

    @Context
    private UriInfo uriInfo;

    @Operation(summary = "Create a comment", description = "Adds a new comment")
    @APIResponse(responseCode = "201", description = "Comment created successfully")
    @APIResponse(responseCode = "400", description = "Invalid comment data")
    @APIResponse(responseCode = "500", description = "Failed to create comment")
    @POST
    public Response createComment(@Valid Comment comment) {
        if (comment == null || comment.getContent() == null ) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid comment data. Content, user, and event are required.")
                    .build();
        }

        Comment created = commentBean.addComment(comment);
        if (created == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create comment due to server error.")
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Operation(summary = "Reply to a comment", description = "Creates a reply to an existing comment")
    @APIResponse(responseCode = "201", description = "Reply created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data provided")
    @APIResponse(responseCode = "404", description = "Parent comment not found")
    @APIResponse(responseCode = "500", description = "Failed to create reply due to server error")
    @POST
    @Path("/reply/{id}")
    public Response createReply(@PathParam("id") int parentCommentId, Comment reply) {
        if (reply == null || reply.getContent() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid reply data. Content, user, and event are required.")
                    .build();
        }

        // Fetch the parent comment
        Comment parentComment = commentBean.getCommentById(parentCommentId);
        if (parentComment == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Parent comment with ID " + parentCommentId + " not found.")
                    .build();
        }

        // Set the parent comment for the reply
        reply.setParentComment(parentComment);

        // Persist the reply
        Comment createdReply = commentBean.addComment(reply);
        if (createdReply == null || createdReply.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create reply due to server error.")
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity(createdReply).build();
    }

    @Operation(summary = "Like a comment", description = "Increments the like count of a specific comment")
    @APIResponse(responseCode = "200", description = "Comment liked successfully")
    @APIResponse(responseCode = "400", description = "Invalid comment ID provided")
    @APIResponse(responseCode = "404", description = "Comment not found")
    @POST
    @Path("/{id}/like")
    public Response likeComment(@PathParam("id") int id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid comment ID. ID must be a positive integer.")
                    .build();
        }

        boolean success = commentBean.likeComment(id);
        if (!success) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Comment with ID " + id + " not found.")
                    .build();
        }

        return Response.ok("Comment liked successfully.").build();
    }

    @Operation(summary = "Get replies to a comment", description = "Fetches all replies to a specific comment")
    @APIResponse(responseCode = "200", description = "List of replies retrieved successfully")
    @APIResponse(responseCode = "400", description = "Invalid comment ID provided")
    @APIResponse(responseCode = "204", description = "No replies found for the comment")
    @GET
    @Path("/{commentId}/replies")
    public Response getReplies(@PathParam("commentId") int commentId) {
        if (commentId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid comment ID. Comment ID must be a positive integer.")
                    .build();
        }

        List<Comment> replies = commentBean.getReplies(commentId);
        if (replies == null || replies.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("No replies found for the given comment ID.")
                    .build();
        }

        return Response.ok(replies).build();
    }

    @Operation(summary = "Delete a comment and all its replies", description = "Deletes a comment and all its associated replies")
    @APIResponse(responseCode = "204", description = "Comment and its replies deleted successfully")
    @APIResponse(responseCode = "400", description = "Invalid comment ID provided")
    @APIResponse(responseCode = "404", description = "Comment not found")
    @DELETE
    @Path("/{commentId}")
    public Response deleteComment(@PathParam("commentId") int commentId) {
        if (commentId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid comment ID. Comment ID must be a positive integer.")
                    .build();
        }

        boolean deleted = commentBean.deleteComment(commentId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Comment with ID " + commentId + " not found.")
                    .build();
        }

        return Response.noContent().build();
    }

}
