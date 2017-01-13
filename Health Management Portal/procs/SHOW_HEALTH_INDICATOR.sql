CREATE OR REPLACE PROCEDURE SHOW_HEALTH_INDICATOR (
   P_ID           IN     JSHARDA.HAS_HEALTH_INDICATOR.PID%TYPE,
   D_NAME         IN     JSHARDA.DISEASE.DISEASE_NAME%TYPE,
   TEST_NAME      IN     JSHARDA.DISEASE.TEST_TYPE%TYPE,
   LOGGED_IN_ID   IN     JSHARDA.HAS_HEALTH_INDICATOR.MODIFIED_BY%TYPE,
   INDI_DID          OUT JSHARDA.HAS_HEALTH_INDICATOR.DID%TYPE,
   INDI_PID          OUT JSHARDA.HAS_HEALTH_INDICATOR.PID%TYPE,
   INDI_MAX          OUT JSHARDA.HAS_HEALTH_INDICATOR.MAX_VALUE%TYPE,
   INDI_MIN          OUT JSHARDA.HAS_HEALTH_INDICATOR.MIN_VALUE%TYPE,
   INDI_FREQ         OUT JSHARDA.HAS_HEALTH_INDICATOR.FREQUENCY%TYPE,
   RET               OUT NUMBER)
IS
   --
   -- Declare program variables as shown below
   -- variable_name data_type
   disease_id      VARCHAR2 (10);
   CURRENT_DATE    TIMESTAMP := LOCALTIMESTAMP;
   IS_VALID_USER   BOOLEAN;
BEGIN
   -- check if logged_in_user is valid - patient itself, or one of its health supporter...
   IS_VALID_USER := CHECK_IF_USER_VALID (P_ID, LOGGED_IN_ID);

   IF (IS_VALID_USER)
   THEN
      -- Get disease id from test type
      SELECT d.DID
        INTO disease_id
        FROM DISEASE d
       WHERE d.TEST_TYPE = TEST_NAME AND d.DISEASE_NAME = D_NAME;

      SELECT DID,
             PID,
             MAX_VALUE,
             MIN_VALUE,
             FREQUENCY
        INTO INDI_DID,
             INDI_PID,
             INDI_MAX,
             INDI_MIN,
             INDI_FREQ
        FROM JSHARDA.HAS_HEALTH_INDICATOR
       WHERE     DID = disease_id
             AND PID = P_ID
             AND (ENDED_ON > CURRENT_TIMESTAMP OR ENDED_ON IS NULL);

      RET := 1;
   ELSE
      RET := -1;
   END IF;
END;                                                               --Procedure
/
