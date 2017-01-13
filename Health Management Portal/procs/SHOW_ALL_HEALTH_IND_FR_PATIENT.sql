CREATE OR REPLACE PROCEDURE SHOW_ALL_HEALTH_IND_FR_PATIENT (
   logged_in_user            IN     JSHARDA.PATIENT.PID%TYPE,
   patient_id                IN     JSHARDA.PATIENT.PID%TYPE,
   health_indicator_cursor      OUT SYS_REFCURSOR,
   RET                          OUT NUMBER)
AS
   is_user_valid   BOOLEAN;
BEGIN
   -- Check if logged_in user is indeeed the HS for the patient_id he inputs.
   is_user_valid := CHECK_IF_USER_VALID (patient_id, logged_in_user);

   IF (is_user_valid)
   THEN
      -- health indicators
      OPEN health_indicator_cursor FOR
         SELECT d.DID,
                d.DISEASE_NAME,
                d.TEST_TYPE,
                h.MAX_VALUE,
                h.MIN_VALUE,
                h.FREQUENCY,
                h.OBSERVED_ON,
                h.RECORDED_ON
           FROM JSHARDA.HAS_HEALTH_INDICATOR h, JSHARDA.DISEASE d
          WHERE     h.PID = patient_id
                AND d.DID = h.DID
                AND (   MAX_VALUE IS NOT NULL
                     OR MIN_VALUE IS NOT NULL
                     OR FREQUENCY IS NOT NULL);

      RET := 1;
   ELSE
      RET := -1;
   END IF;
END;
/
