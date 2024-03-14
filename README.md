# Dirscanner

This is a simple directory scanner that scans a directory every few minutes and writes file metadata into a database and features a REST-API to access it. The number of minutes between scans can be configured.

## Running the application

To run the application in a dockerized setup, simply use `docker compose up` in the root of the project.

## Configuration
Configurations are done in the `.env` file. The root directory to scan can be configured with `DIRSCANNER_ROOT`. The delay between scans can be configured with `DIRSCANNER_DELAY_MINUTES`.
