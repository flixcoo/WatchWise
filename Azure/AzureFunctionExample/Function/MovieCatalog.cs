using System.Linq;
using System.Net;
using Azure.Data.Tables;
using AzureFunctionExample.Model;
using Microsoft.Azure.Functions.Worker;
using Microsoft.Azure.Functions.Worker.Http;
using Microsoft.Azure.WebJobs.Extensions.OpenApi.Core.Attributes;
using Microsoft.Azure.WebJobs.Extensions.OpenApi.Core.Enums;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;

namespace AzureFunctionExample;

public class MovieCatalog
{
    private readonly ILogger _logger;
    private readonly TableClient _table;

    public MovieCatalog(ILoggerFactory loggerFactory, TableServiceClient tableService)
    {
        string tableName = "movies";
        _logger = loggerFactory.CreateLogger<MovieCatalog>();
        tableService.CreateTableIfNotExists(tableName);
        _table = tableService.GetTableClient(tableName);
    }


    // *** Register User ***
    [OpenApiOperation(
        operationId: "registerUser",
        tags: ["users"],
        Summary = "Register a new user",
        Description = "Creates a new user in the system."
    )]
    [OpenApiRequestBody(contentType: "application/json", bodyType: typeof(UserModel))]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(UserModel),
        Summary = "Returns the registered user."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.Conflict,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "User already exists."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.BadRequest,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Invalid input data."
    )]
    [OpenApiSecurity("function_key", SecuritySchemeType.ApiKey, Name = "code", In = OpenApiSecurityLocationType.Query)]
    [Function("registerUser")]
    public async Task<HttpResponseData> RegisterUser(
        [HttpTrigger(AuthorizationLevel.Function, "post", Route = "user/register")] HttpRequestData request)
    {
        _logger.LogInformation("Processing user registration request...");
        var user = await request.ReadFromJsonAsync<UserModel>();

        if (user == null || string.IsNullOrEmpty(user.Username) || string.IsNullOrEmpty(user.PasswordHash))
        {
            var response = request.CreateResponse(HttpStatusCode.BadRequest);
            await response.WriteAsJsonAsync(new { Error = "Invalid input data." });
            return response;
        }

        var existingUser = await _table.GetEntityIfExistsAsync<UserTableModel>("Users", user.Username);
        if (existingUser.HasValue)
        {
            var conflictResponse = request.CreateResponse(HttpStatusCode.Conflict);
            await conflictResponse.WriteAsJsonAsync(new { Error = "User already exists." });
            return conflictResponse;
        }

        var tableEntity = new UserTableModel
        {
            PartitionKey = "Users",
            RowKey = user.Username,
            PasswordHash = user.PasswordHash
        };

        await _table.AddEntityAsync(tableEntity);
        return request.CreateResponse(HttpStatusCode.OK);
    }



    [OpenApiOperation(
    operationId: "loginUser",
    tags: ["users"],
    Summary = "Login user",
    Description = "Authenticates a user based on User ID and password."
    )]
    [OpenApiRequestBody(contentType: "application/json", bodyType: typeof(UserModel))]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(UserModel),
        Summary = "Returns the authenticated user."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.Unauthorized,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Unauthorized if credentials are incorrect."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("loginUser")]
    public async Task<HttpResponseData> LoginUser(
        [HttpTrigger(AuthorizationLevel.Function, "post", Route = "user/login")] HttpRequestData request)
    {
        var loginRequest = await request.ReadFromJsonAsync<UserModel>();

        if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Username) || string.IsNullOrEmpty(loginRequest.PasswordHash))
            return request.CreateResponse(HttpStatusCode.BadRequest);

        var user = await _table.GetEntityIfExistsAsync<UserTableModel>("Users", loginRequest.Username);

        if (!user.HasValue || user.Value.PasswordHash != loginRequest.PasswordHash)
            return request.CreateResponse(HttpStatusCode.Unauthorized);

        return request.CreateResponse(HttpStatusCode.OK);
    }

    


    [OpenApiOperation(
        operationId: "deleteUser",
        tags: ["users"],
        Summary = "Delete a user",
        Description = "Deletes a user by username."
    )]
    [OpenApiParameter(name: "username", In = ParameterLocation.Path, Required = true, Type = typeof(string), Summary = "The username to delete")]
    [OpenApiResponseWithoutBody(
        statusCode: HttpStatusCode.NoContent,
        Summary = "User successfully deleted."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.NotFound,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "User not found."
    )]
    [OpenApiSecurity("function_key", SecuritySchemeType.ApiKey, Name = "code", In = OpenApiSecurityLocationType.Query)]
    [Function("deleteUser")]
    public async Task<HttpResponseData> DeleteUser(
        [HttpTrigger(AuthorizationLevel.Function, "delete", Route = "user/delete/{username}")] HttpRequestData request,
        string username)
    {
        var user = await _table.GetEntityIfExistsAsync<UserTableModel>("Users", username);

        if (!user.HasValue)
            return request.CreateResponse(HttpStatusCode.NotFound);

        await _table.DeleteEntityAsync("Users", username);
        return request.CreateResponse(HttpStatusCode.NoContent);
    }


    [OpenApiOperation(
    operationId: "updateUserPassword",
    tags: ["users"],
    Summary = "Update user password",
    Description = "Updates the password for a given user."
    )]
    [OpenApiRequestBody(contentType: "application/json", bodyType: typeof(UserModel), Required = true)]
    [OpenApiResponseWithoutBody(
        statusCode: HttpStatusCode.NoContent,
        Summary = "Password updated successfully."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.NotFound,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "User not found."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.BadRequest,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Invalid input data."
    )]
    [OpenApiSecurity("function_key", SecuritySchemeType.ApiKey, Name = "code", In = OpenApiSecurityLocationType.Query)]
    [Function("updateUserPassword")]
    public async Task<HttpResponseData> UpdateUserPassword(
        [HttpTrigger(AuthorizationLevel.Function, "put", Route = "user/updatePassword")] HttpRequestData request)
    {
        _logger.LogInformation("Processing password update request...");
        var user = await request.ReadFromJsonAsync<UserModel>();

        if (user == null || string.IsNullOrEmpty(user.Username) || string.IsNullOrEmpty(user.PasswordHash))
        {
            var response = request.CreateResponse(HttpStatusCode.BadRequest);
            await response.WriteAsJsonAsync(new { Error = "Invalid input data." });
            return response;
        }

        var existingUser = await _table.GetEntityIfExistsAsync<UserTableModel>("Users", user.Username);
        if (!existingUser.HasValue)
        {
            var notFoundResponse = request.CreateResponse(HttpStatusCode.NotFound);
            await notFoundResponse.WriteAsJsonAsync(new { Error = "User not found." });
            return notFoundResponse;
        }

        var updatedUser = existingUser.Value;
        updatedUser.PasswordHash = user.PasswordHash;

        await _table.UpdateEntityAsync(updatedUser, updatedUser.ETag, TableUpdateMode.Replace);
        return request.CreateResponse(HttpStatusCode.NoContent);
    }



    [OpenApiOperation(
    operationId: "addMovieToCategory",
    tags: ["movies"],
    Summary = "Add a movie to a category",
    Description = "Links a movie to a user-defined category."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(FilmCategoryTableModel),
        Summary = "Movie successfully added to category."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.Conflict,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Movie already exists in the category."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("addMovieToCategory")]
    public async Task<HttpResponseData> AddMovieToCategory(
        [HttpTrigger(AuthorizationLevel.Function, "post", Route = "movie/{userName}/{category}/{filmId}")] HttpRequestData request,
        string userName, string category, string filmId)
    {
        var existingEntry = await _table.GetEntityIfExistsAsync<FilmCategoryTableModel>(userName, $"{category}_{filmId}");

        if (existingEntry.HasValue)
        {
            var response = request.CreateResponse(HttpStatusCode.Conflict);
            await response.WriteAsJsonAsync(new { Error = "Movie already exists in this category." });
            return response;
        }

        var entity = new FilmCategoryTableModel(userName, category, filmId);
        await _table.AddEntityAsync(entity);
        return request.CreateResponse(HttpStatusCode.OK);
    }



    [OpenApiOperation(
    operationId: "deleteMovieFromCategory",
    tags: ["movies"],
    Summary = "Remove a movie from a category",
    Description = "Removes the association between a movie and a category for a user."
    )]
    [OpenApiResponseWithoutBody(
        statusCode: HttpStatusCode.NoContent,
        Summary = "Movie successfully removed from category."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.NotFound,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Movie was not found in the category."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("deleteMovieFromCategory")]
    public async Task<HttpResponseData> DeleteMovieFromCategory(
        [HttpTrigger(AuthorizationLevel.Function, "delete", Route = "movie/{userName}/{category}/{filmId}")] HttpRequestData request,
        string userName, string category, string filmId)
    {
        var entity = await _table.GetEntityIfExistsAsync<FilmCategoryTableModel>(userName, $"{category}_{filmId}");

        if (!entity.HasValue)
        {
            var response = request.CreateResponse(HttpStatusCode.NotFound);
            await response.WriteAsJsonAsync(new { Error = "Movie not found in category." });
            return response;
        }

        await _table.DeleteEntityAsync(userName, $"{category}_{filmId}");
        return request.CreateResponse(HttpStatusCode.NoContent);
    }


    [OpenApiOperation(
    operationId: "likeMovie",
    tags: ["movies"],
    Summary = "Like a movie",
    Description = "Marks a movie as liked by a user."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(FilmTableModel),
        Summary = "Movie successfully liked."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.NotFound,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Movie not found in user's collection."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("likeMovie")]
    public async Task<HttpResponseData> LikeMovie(
        [HttpTrigger(AuthorizationLevel.Function, "post", Route = "movie/like/{userName}/{filmId}")] HttpRequestData request,
        string userName, string filmId)
    {
        var movie = await _table.GetEntityIfExistsAsync<FilmTableModel>(userName, filmId);

        if (!movie.HasValue)
            return request.CreateResponse(HttpStatusCode.NotFound);

        var updatedMovie = movie.Value;
        updatedMovie.IsLiked = true;

        await _table.UpdateEntityAsync(updatedMovie, updatedMovie.ETag, TableUpdateMode.Replace);
        return request.CreateResponse(HttpStatusCode.OK);
    }


    [OpenApiOperation(
    operationId: "dislikeMovie",
    tags: ["movies"],
    Summary = "Remove like from a movie",
    Description = "Unmarks a movie as liked by a user."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(FilmTableModel),
        Summary = "Movie successfully unliked."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.NotFound,
        contentType: "application/json",
        bodyType: typeof(ErrorModel),
        Summary = "Movie not found in user's collection."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("dislikeMovie")]
    public async Task<HttpResponseData> DislikeMovie(
        [HttpTrigger(AuthorizationLevel.Function, "post", Route = "movie/dislike/{userName}/{filmId}")] HttpRequestData request,
        string userName, string filmId)
    {
        var movie = await _table.GetEntityIfExistsAsync<FilmTableModel>(userName, filmId);

        if (!movie.HasValue)
            return request.CreateResponse(HttpStatusCode.NotFound);

        var updatedMovie = movie.Value;
        updatedMovie.IsLiked = false;

        await _table.UpdateEntityAsync(updatedMovie, updatedMovie.ETag, TableUpdateMode.Replace);
        return request.CreateResponse(HttpStatusCode.OK);
    }


    [OpenApiOperation(
    operationId: "getAllMoviesFromCategory",
    tags: ["movies"],
    Summary = "Get all movies from a category",
    Description = "Returns all movies assigned to a specific category for a user."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(List<string>),
        Summary = "List of movies in the category."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("getAllMoviesFromCategory")]
    public async Task<HttpResponseData> GetAllMoviesFromCategory(
        [HttpTrigger(AuthorizationLevel.Function, "get", Route = "movies/{userName}/{category}")] HttpRequestData request,
        string userName, string category)
    {
        var categoryMovies = _table.Query<FilmCategoryTableModel>(
            filter: $"PartitionKey eq '{userName}' and CategoryName eq '{category}'"
        );

        var movieList = categoryMovies.Select(m => m.FilmId).ToList();

        var response = request.CreateResponse(HttpStatusCode.OK);
        await response.WriteAsJsonAsync(movieList);
        return response;
    }


    [OpenApiOperation(
    operationId: "getAllMovies",
    tags: ["movies"],
    Summary = "Get all movies for a user",
    Description = "Returns all movies a user has stored in their collection."
    )]
    [OpenApiResponseWithBody(
        statusCode: HttpStatusCode.OK,
        contentType: "application/json",
        bodyType: typeof(List<FilmTableModel>),
        Summary = "List of all movies for the user."
    )]
    [OpenApiSecurity(
        "function_key",
        SecuritySchemeType.ApiKey,
        Name = "code",
        In = OpenApiSecurityLocationType.Query
    )]
    [Function("getAllMovies")]
    public async Task<HttpResponseData> GetAllMovies(
        [HttpTrigger(AuthorizationLevel.Function, "get", Route = "movies/{userName}")] HttpRequestData request,
        string userName)
    {
        var movies = _table.Query<FilmTableModel>(filter: $"PartitionKey eq '{userName}'");

        var response = request.CreateResponse(HttpStatusCode.OK);
        await response.WriteAsJsonAsync(movies);
        return response;
    }
}
