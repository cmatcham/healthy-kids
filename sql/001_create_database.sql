CREATE USER healthykids WITH PASSWORD 'k33pMeH34lthy';

CREATE DATABASE healthykids
  WITH OWNER = healthykids;

GRANT ALL PRIVILEGES ON DATABASE healthykids TO healthykids;