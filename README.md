# IMDB ACADEMY PROJECT
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
Once the system is deployed, you can find all the API documentation here:
```
http://localhost:8080/swagger-ui/index.html
```

## Asynchronous jobs
With the system deployed, you can check the status of asynchronous jobs, such as indexing, in the following link:
```
http://localhost:8000/dashboard/overview
```
Besides, you can check indexing progress connecting to localhost:9200 through:
```
app.elasticvue.com
```
