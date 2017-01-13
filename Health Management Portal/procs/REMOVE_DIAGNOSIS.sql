CREATE OR REPLACE PROCEDURE REMOVE_DIAGNOSIS (
   PATIENT_ID     IN     JSHARDA.PATIENT.PID%TYPE,
   LOGGED_IN_ID   IN     JSHARDA.PATIENT.PID%TYPE,
   D_NAME         IN     JSHARDA.DISEASE.DISEASE_NAME%TYPE,
   RET               OUT NUMBER -- this value is 0 if there is no health supporter, else greater than 0
                               )
IS
   D_COUNT         NUMBER;

   --
   -- Declare program variables as shown below
   -- variable_name data_type

   CURSOR disease_details
   IS
      SELECT *
        FROM JSHARDA.DISEASE
       WHERE DISEASE_NAME = D_NAME;

   IS_VALID_USER   BOOLEAN;
BEGIN
   IS_VALID_USER := CHECK_IF_USER_VALID (PATIENT_ID, LOGGED_IN_ID);

   RET := GET_HS_COUNT (PATIENT_ID);

   IF (IS_VALID_USER)
   THEN
      FOR d IN disease_details
      LOOP
         UPDATE JSHARDA.HAS_HEALTH_INDICATOR
            SET ENDED_ON = CURRENT_TIMESTAMP
          WHERE     DID = d.DID
                AND PID = PATIENT_ID
                AND (ENDED_ON > CURRENT_TIMESTAMP OR ENDED_ON IS NULL);
      END LOOP;


      SELECT COUNT (*)
        INTO D_COUNT
        FROM JSHARDA.HAS_HEALTH_INDICATOR
       WHERE    PID = PATIENT_ID AND ENDED_ON > CURRENT_TIMESTAMP
             OR ENDED_ON IS NULL;

      IF D_COUNT = 0
      THEN
         UPDATE JSHARDA.PATIENT
            SET CATEGORY = 'W'
          WHERE PID = PATIENT_ID;
      END IF;
   END IF;

   IF (NOT IS_VALID_USER)
   THEN
      RET := -1;
   END IF;
/* EXCEPTION
 WHEN exception_name THEN
  --statements; */
END;                                                               --Procedure
/
