CREATE OR REPLACE PROCEDURE UPDATE_HEALTH_INDICATOR (
   patient_id       IN     JSHARDA.HAS_HEALTH_INDICATOR.PID%TYPE,
   d_name           IN     JSHARDA.DISEASE.DISEASE_NAME%TYPE,
   test_name        IN     JSHARDA.DISEASE.TEST_TYPE%TYPE,
   max_value        IN     JSHARDA.HAS_HEALTH_INDICATOR.MAX_VALUE%TYPE,
   min_value        IN     JSHARDA.HAS_HEALTH_INDICATOR.MIN_VALUE%TYPE,
   freq             IN     JSHARDA.HAS_HEALTH_INDICATOR.FREQUENCY%TYPE,
   logged_in_user   IN     JSHARDA.HAS_HEALTH_INDICATOR.MODIFIED_BY%TYPE,
   RET                 OUT NUMBER)
AS
   disease_id         VARCHAR2 (10);
   CURRENT_DATE       TIMESTAMP := LOCALTIMESTAMP;
   date_of_sickness   DATE;
   IS_VALID_USER      BOOLEAN;
BEGIN
   -- check if logged_in_user is valid - patient itself, or one of its health supporter...
   IS_VALID_USER := CHECK_IF_USER_VALID (patient_id, logged_in_user);

   RET := 0;

   -- check if max_value is greater than min_value
   IF (max_value > min_value)
   THEN
      RET := 1;
   END IF;

   IF (RET > 0 AND IS_VALID_USER)
   THEN
      -- Get disease id from test type
      SELECT d.DID
        INTO disease_id
        FROM DISEASE d
       WHERE d.TEST_TYPE = test_name AND d.DISEASE_NAME = d_name;

      -- Fetch the previous indicator for that patient and disease id and whose enddate is null.
      SELECT h.OBSERVED_ON
        INTO date_of_sickness
        FROM JSHARDA.HAS_HEALTH_INDICATOR h
       WHERE h.DID = disease_id AND h.PID = patient_id AND h.ENDED_ON IS NULL;

      -- Update the end date of the previous entry to the current system date.
      UPDATE JSHARDA.HAS_HEALTH_INDICATOR h
         SET h.ENDED_ON = CURRENT_DATE
       WHERE h.PID = patient_id AND h.DID = disease_id AND h.ENDED_ON IS NULL;

      INSERT INTO HAS_HEALTH_INDICATOR (PID,
                                        DID,
                                        MAX_VALUE,
                                        MIN_VALUE,
                                        FREQUENCY,
                                        RECORDED_ON,
                                        OBSERVED_ON,
                                        MODIFIED_BY,
                                        ENDED_ON)
           VALUES (patient_id,
                   disease_id,
                   max_value,
                   min_value,
                   freq,
                   CURRENT_DATE,
                   date_of_sickness,
                   logged_in_user,
                   NULL);

      RET := 1;
   ELSE
      IF (max_value <= min_value)
      THEN
         RET := -2;
      ELSE
         RET := -1;
      END IF;
   END IF;
END;
/
