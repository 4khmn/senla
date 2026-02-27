@echo off

SET PGPASSWORD=postgres
SET PSQL="C:\Program Files\PostgreSQL\17\bin\psql.exe"
echo Creating database...
%PSQL% -U postgres -c "CREATE DATABASE garage_db;"


echo Creating tables...
%PSQL% -U postgres -d garage_db -f db\ddl\master.sql
%PSQL% -U postgres -d garage_db -f db\ddl\garage_spot.sql
%PSQL% -U postgres -d garage_db -f db\ddl\orders.sql

echo Inserting test data...
%PSQL% -U postgres -d garage_db -f db\dml\master_data.sql
%PSQL% -U postgres -d garage_db -f db\dml\garage_spot_data.sql
%PSQL% -U postgres -d garage_db -f db\dml\orders_data.sql

echo DONE
pause