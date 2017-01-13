CREATE OR REPLACE PROCEDURE ADD_HS (
   HS_ID        IN     JSHARDA.HEALTH_SUPPORTER.HSID%TYPE,
   PATIENT_ID   IN     JSHARDA.PATIENT.PID%TYPE,
   AUTHR_DATE   IN     DATE,
   RET             OUT NUMBER   -- -1 IF HS_ID IS NOT IN SYSTEM(PATIENT TABLE)
                             )
IS
   SUPP_TYPE   CHAR (1);

   --
   -- Declare program variables as shown below
   -- variable_name data_type
   CURSOR PATIENT_HS
   IS
      SELECT *
        FROM JSHARDA.HEALTH_SUPPORTER
       WHERE     PATIENT_PID = PATIENT_ID
             AND (ENDDATE IS NULL OR ENDDATE > CURRENT_TIMESTAMP);
BEGIN
   RET := 1;

   IF (NOT IS_PRESENT_IN_PATIENTS (HS_ID))
   THEN
      RET := -1;
   ELSIF (GET_HS_COUNT (PATIENT_ID) = 2)
   THEN
      RET := -2;
   ELSIF (HS_ID = PATIENT_ID)
   THEN
      RET := 0;
   ELSIF (GET_HS_COUNT (PATIENT_ID) = 0)
   THEN
      SUPP_TYPE := 'P';
   ELSE
      SUPP_TYPE := 'S';
   END IF;

   FOR HS IN PATIENT_HS
   LOOP
      IF (HS.HSID = HS_ID)
      THEN
         RET := -3;                                    -- IF HS ALREADY EXISTS
      END IF;
   END LOOP;

   IF (RET > 0)
   THEN
      INSERT INTO JSHARDA.HEALTH_SUPPORTER (HSID,
                                            PATIENT_PID,
                                            AUTHDATE,
                                            ENDDATE,
                                            SUPPORTER_TYPE)
           VALUES (HS_ID,
                   PATIENT_ID,
                   TO_TIMESTAMP (AUTHR_DATE),
                   '',
                   SUPP_TYPE);
   END IF;
END;                                                               --Procedure
/
