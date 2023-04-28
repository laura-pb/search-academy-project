# IMDB ACADEMY PROJECT
This is an IMDb search engine backend project to explore the different technologies used (Springboot, Elasticsearch,
Docker).  

I researched ways to index and query documents to obtain relevant results and decided to go for a double approach: 
exact match and ngrams. I used index mapping to store movie titles in both ways and then configured queries
to boost certain results over others.

Besides, with the goal of reducing time wasted by users deciding what to watch, I implemented an initial approach to 
"daily movie", a system that recommends a movie a day based on user liked movies (all in session). An external API is used 
to retrieve movies related to a movie.
## Installation
1. Clone the repository in your machine
```
git clone https://github.com/laura-pb/search-academy-project
```
2. Go to the project folder
```
cd search-academy-project
```
3. Build and deploy the application
```
docker compose up --build -d
```
If you want to stop the application
```
docker compose down
```

## Documentation
Once the system is deployed, all the API documentation can be found here:
```
http://localhost:8080/swagger-ui/index.html
```

## Asynchronous jobs
With the system deployed, the status of asynchronous jobs (indexing) can be checked in the following link:
```
http://localhost:8000/dashboard/overview
```
Besides, you can check indexing progress connecting to localhost:9200 through:
```
app.elasticvue.com
```
