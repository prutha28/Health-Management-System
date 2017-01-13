CREATE OR REPLACE PROCEDURE generate_alertIDs
IS
   --
   -- Declare program variables as shown below
   -- variable_name data_type


   CURSOR diseases
   IS
      SELECT * FROM JSHARDA.DISEASE;

   ID   NUMBER;
BEGIN
   ID := 1;

   FOR D IN diseases
   LOOP
      INSERT INTO JSHARDA.ALERT (ALERT_ID, DID, ALERT_TYPE)
           VALUES (CAST (ID AS VARCHAR (10)), D.DID, 'L');

      ID := ID + 1;

      INSERT INTO JSHARDA.ALERT (ALERT_ID, DID, ALERT_TYPE)
           VALUES (CAST (ID AS VARCHAR (10)), D.DID, 'O');

      ID := ID + 1;
   END LOOP;
END;                                                               --Procedure
/
