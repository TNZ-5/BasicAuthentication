<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movie Recommendations</title>
</head>
<body>

<style>
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
    }

    h1 {
        color: #333;
    }

    h2 {
        color: #666;
    }

    form {
        margin-bottom: 20px;
    }

    label {
        display: block;
        margin-bottom: 5px;
    }

    input[type="text"],
    textarea {
        width: 300px;
        padding: 5px;
        margin-bottom: 10px;
    }

    button[type="submit"] {
        padding: 5px 10px;
        background-color: #333;
        color: #fff;
        border: none;
        cursor: pointer;
    }

    #movies-list {
        margin-top: 20px;
    }

    #movies-list div {
        margin-bottom: 5px;
    }

    #movies-list div:last-child {
        margin-bottom: 0;
    }

</style>


<h1>Movie Recommendations</h1>

<h2>Top Movies</h2>
<div id="movies-list">
    <!-- The list of movies will be populated dynamically -->
</div>

<form id="create-movie-form">
    <h2>Create New Movie Entry</h2>
    <label for="title">Title:</label>
    <input type="text" id="title" required><br>
    <label for="description">Description:</label>
    <textarea id="description" required></textarea><br>
    <label for="releaseYear">Release Year:</label>
    <input type="text" id="releaseYear" required><br>
    <label for="genres">Genres:</label>
    <input type="text" id="genres" required><br>
    <label for="userRatings">User Ratings:</label>
    <input type="text" id="userRatings" required><br>
    <button type="submit">Create</button>
</form>

<h2>Change Movie Entry</h2>
<form id="change-movie-form">
    <label for="change-movie-id">Movie ID:</label>
    <input type="text" id="change-movie-id" required><br>
    <label for="change-movie-title">Title:</label>
    <input type="text" id="change-movie-title"><br>
    <label for="change-movie-description">Description:</label>
    <textarea id="change-movie-description"></textarea><br>
    <label for="change-movie-releaseYear">Release Year:</label>
    <input type="text" id="change-movie-releaseYear"><br>
    <label for="change-movie-genres">Genres:</label>
    <input type="text" id="change-movie-genres"><br>
    <label for="change-movie-userRatings">User Ratings:</label>
    <input type="text" id="change-movie-userRatings"><br>
    <button type="submit">Change</button>
</form>

<h2>Delete Movie Entry</h2>
<form id="delete-movie-form">
    <label for="delete-movie-id">Movie ID:</label>
    <input type="text" id="delete-movie-id" required><br>
    <button type="submit">Delete</button>
</form>

<script>
    // Function to fetch and display movies from the server
    function fetchMovies() {
        fetch('api/movie-recommendations')
            .then(response => response.json())
            .then(data => {
                const moviesList = document.getElementById('movies-list');
                moviesList.innerHTML = ''; // Clear the existing list
                data.forEach(movie => {
                    const movieItem = document.createElement('div');
                    const movieLink = document.createElement('a');
                    movieLink.textContent = `${movie}`;
                    movieLink.href = `http://localhost:8080/demoProjectEmpSys_war/movieDetails`;
                    movieItem.appendChild(movieLink);
                    moviesList.appendChild(movieItem);
                });
            })
            .catch(error => console.error('Error fetching movies:', error));
    }

    // Function to create a new movie entry
    function createMovieEntry(event) {
        event.preventDefault();
        const form = document.getElementById('create-movie-form');
        const title = document.getElementById('title').value;
        const description = document.getElementById('description').value;
        const releaseYear = document.getElementById('releaseYear').value;
        const genres = document.getElementById('genres').value;
        const userRatings = document.getElementById('userRatings').value;

        const movie = {
            title: title,
            description: description,
            releaseYear: releaseYear,
            genres: genres,
            userRatings: userRatings
        };

        fetch('api/movie-recommendations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(movie)
        })
            .then(response => response.json())
            .then(data => {
                console.log('New movie entry created:', data);
                form.reset(); // Reset the form fields
                fetchMovies(); // Refresh the movies list
            })
            .catch(error => console.error('Error creating movie entry:', error));
    }

    // Function to change a movie entry
    function changeMovieEntry(event) {
        event.preventDefault();
        const form = document.getElementById('change-movie-form');
        const movieId = document.getElementById('change-movie-id').value;
        const title = document.getElementById('change-movie-title').value;
        const description = document.getElementById('change-movie-description').value;
        const releaseYear = document.getElementById('change-movie-releaseYear').value;
        const genres = document.getElementById('change-movie-genres').value;
        const userRatings = document.getElementById('change-movie-userRatings').value;

        const movie = {
            id: movieId,
            title: title,
            description: description,
            releaseYear: releaseYear,
            genres: genres,
            userRatings: userRatings
        };

        fetch('api/movie-recommendations', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(movie)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Movie entry changed successfully');
                    form.reset(); // Reset the form fields
                    fetchMovies(); // Refresh the movies list
                } else {
                    console.error('Error changing movie entry:', response.statusText);
                }
            })
            .catch(error => console.error('Error changing movie entry:', error));
    }

    // Function to delete a movie entry
    function deleteMovieEntry(event) {
        event.preventDefault();
        const form = document.getElementById('delete-movie-form');
        const movieId = document.getElementById('delete-movie-id').value;

        fetch(`api/movie-recommendations/${movieId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    console.log('Movie entry deleted successfully');
                    form.reset(); // Reset the form fields
                    fetchMovies(); // Refresh the movies list
                } else {
                    console.error('Error deleting movie entry:', response.statusText);
                }
            })
            .catch(error => console.error('Error deleting movie entry:', error));
    }

    // Attach event listeners to the create, change, and delete movie forms
    const createMovieForm = document.getElementById('create-movie-form');
    const changeMovieForm = document.getElementById('change-movie-form');
    const deleteMovieForm = document.getElementById('delete-movie-form');
    createMovieForm.addEventListener('submit', createMovieEntry);
    changeMovieForm.addEventListener('submit', changeMovieEntry);
    deleteMovieForm.addEventListener('submit', deleteMovieEntry);

    // Fetch movies when the page loads
    fetchMovies();
</script>
</body>
</html>
