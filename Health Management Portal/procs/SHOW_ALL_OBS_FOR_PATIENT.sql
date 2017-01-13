CREATE OR REPLACE PROCEDURE SHOW_ALL_OBS_FOR_PATIENT (
   logged_in_user       IN     JSHARDA.PATIENT.PID%TYPE,
   patient_id           IN     JSHARDA.HAS_DISEASE_OBSERVATION.PID%TYPE,
   observation_cursor      OUT SYS_REFCURSOR,
   RET                     OUT NUMBER)
IS
   is_user_valid   BOOLEAN;
--
-- Declare program variables as shown below
-- variable_name data_type

BEGIN
   -- Check if logged_in user is indeeed the HS for the patient_id he inputs.
   is_user_valid := CHECK_IF_USER_VALID (patient_id, logged_in_user);

   IF (is_user_valid)
   THEN
      OPEN observation_cursor FOR
         SELECT D.DISEASE_NAME,
                D.TEST_TYPE,
                O.OBSERVED_ON,
                O.RECORDED_ON,
                O.IS_VIOLATED,
                O.ADDED_BY
           FROM JSHARDA.HAS_DISEASE_OBSERVATION O, JSHARDA.DISEASE D
          WHERE O.PID = patient_id AND D.DID = O.DID;

      RET := 1;
   ELSE
      RET := -1;
   END IF;
END;                                                               --Procedure
/
