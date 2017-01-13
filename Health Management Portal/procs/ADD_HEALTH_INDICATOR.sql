CREATE OR REPLACE PROCEDURE ADD_HEALTH_INDICATOR (
   patient_id       IN     JSHARDA.HAS_HEALTH_INDICATOR.PID%TYPE,
   d_name           IN     JSHARDA.DISEASE.DISEASE_NAME%TYPE,
   test_name        IN     JSHARDA.DISEASE.TEST_TYPE%TYPE,
   freq             IN     JSHARDA.HAS_HEALTH_INDICATOR.FREQUENCY%TYPE,
   max_value        IN     JSHARDA.HAS_HEALTH_INDICATOR.MAX_VALUE%TYPE,
   min_value        IN     JSHARDA.HAS_HEALTH_INDICATOR.MIN_VALUE%TYPE,
   obs_on           IN     JSHARDA.HAS_HEALTH_INDICATOR.OBSERVED_ON%TYPE,
   logged_in_user   IN     JSHARDA.HAS_HEALTH_INDICATOR.MODIFIED_BY%TYPE,
   RET                 OUT NUMBER)
-- RETURN VALUE DESCRIPTION
-- -3 - Health Indicator entry already added
-- -2 - MAX !> MIN
-- -1 - Logged in User is not valid to add Health Indicator
-- 1 - Corectly added Health Indicator

AS
   disease_id      VARCHAR2 (10);
   CURRENT_DATE    TIMESTAMP := LOCALTIMESTAMP;
   IS_VALID_USER   BOOLEAN;
   IS_HI_ADDED     BOOLEAN;
BEGIN
   -- check if logged_in_user is valid - patient itself, or one of its health supporter...
   IS_VALID_USER := CHECK_IF_USER_VALID (patient_id, logged_in_user);

   IS_HI_ADDED := IF_HI_ALREADY_ADDDED (patient_id, d_name, test_name);

   RET := 0;

   -- check if max_value is greater than min_value
   IF (max_value > min_value)
   THEN
      RET := 1;
   END IF;


   IF (RET > 0 AND IS_VALID_USER AND (NOT IS_HI_ADDED))
   THEN
      -- Get disease id from test type
      SELECT d.DID
        INTO disease_id
        FROM DISEASE d
       WHERE d.TEST_TYPE = test_name AND d.DISEASE_NAME = d_name;

      INSERT INTO HAS_HEALTH_INDICATOR (DID,
                                        ENDED_ON,
                                        FREQUENCY,
                                        MAX_VALUE,
                                        MIN_VALUE,
                                        MODIFIED_BY,
                                        OBSERVED_ON,
                                        PID,
                                        RECORDED_ON)
           VALUES (disease_id,
                   NULL,
                   freq,
                   max_value,
                   min_value,
                   logged_in_user,
                   obs_on,
                   patient_id,
                   CURRENT_DATE);

      RET := 1;
   ELSE
      IF (max_value <= min_value)
      THEN
         RET := -2;
      ELSIF (IS_HI_ADDED)
      THEN
         RET := -3;
      ELSE
         RET := -1;
      END IF;
   END IF;
END;
/
