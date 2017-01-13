CREATE OR REPLACE PROCEDURE REMOVE_HEALTH_INDICATOR (
   PATIENT_ID       IN     JSHARDA.PATIENT.PID%TYPE,
   TEST_NAME        IN     DISEASE.TEST_TYPE%TYPE,
   LOGGED_IN_USER   IN     JSHARDA.PATIENT.PID%TYPE,
   RET                 OUT NUMBER                 -- IF INVALID USER -1 ELSE 1
                                 )
IS
   --
   -- Declare program variables as shown below
   -- variable_name data_type
   disease_id   VARCHAR2 (10);
BEGIN
   RET := -1;

   IF (CHECK_IF_USER_VALID (PATIENT_ID, LOGGED_IN_USER))
   THEN
      SELECT DID
        INTO disease_id
        FROM JSHARDA.DISEASE
       WHERE DISEASE_NAME = 'GENERAL' AND TEST_TYPE = TEST_NAME;


      UPDATE JSHARDA.HAS_HEALTH_INDICATOR
         SET MODIFIED_BY = LOGGED_IN_USER, ENDED_ON = CURRENT_TIMESTAMP
       WHERE     DID = disease_id
             AND PID = PATIENT_ID
             AND (ENDED_ON > CURRENT_TIMESTAMP OR ENDED_ON IS NULL);

      RET := 1;
   ELSE
      RET := -1;
   END IF;
END;                                                               --Procedure
/
