CREATE OR REPLACE PROCEDURE ADD_HEALTH_OBSERVATION (
   added_by      IN     JSHARDA.HAS_DISEASE_OBSERVATION.ADDED_BY%TYPE,
   test_name     IN     JSHARDA.DISEASE.TEST_TYPE%TYPE,
   patient_id    IN     JSHARDA.HAS_DISEASE_OBSERVATION.PID%TYPE,
   observed_on   IN     HAS_DISEASE_OBSERVATION.OBSERVED_ON%TYPE,
   val           IN     JSHARDA.HAS_DISEASE_OBSERVATION.VAL%TYPE,
   RET              OUT NUMBER)
IS
   is_violated                   CHAR;
   disease_id                    VARCHAR2 (10);
   recorded_on                   TIMESTAMP;
   ALT_id                        VARCHAR2 (10);
   a_id                          VARCHAR2 (10);
   return_code_for_clear_alert   NUMBER; -- to clear low frequency alert when observation is added

   CURSOR diseases
   IS
      SELECT *
        FROM JSHARDA.DISEASE
       WHERE     TEST_TYPE = test_name
             AND DISEASE_NAME IN
                    (SELECT UNIQUE (DISEASE_NAME)
                       FROM JSHARDA.DISEASE
                      WHERE DID IN
                               (SELECT DID
                                  FROM JSHARDA.HAS_HEALTH_INDICATOR
                                 WHERE     PID = PATIENT_ID
                                       AND (   ENDED_ON > CURRENT_TIMESTAMP
                                            OR ENDED_ON IS NULL)));
BEGIN
   FOR dv IN diseases
   LOOP
      -- Fetch the disease id based on type.
      SELECT d.DID
        INTO disease_id
        FROM DISEASE d
       WHERE d.TEST_TYPE = test_name AND d.DISEASE_NAME = dv.DISEASE_NAME;

      recorded_on := LOCALTIMESTAMP;

      IF (IS_VALUE_VIOLATED (val,
                             patient_id,
                             dv.DISEASE_NAME,
                             test_name) = 'Y')
      THEN
         -- alert id
         SELECT a.ALERT_ID
           INTO ALT_id
           FROM JSHARDA.ALERT a
          WHERE a.DID = disease_id AND a.ALERT_TYPE = 'O';

         -- Throw alert
         UPDATE JSHARDA.HAS_ALERT
            SET STATUS = 'I', CLEARED_AT = CURRENT_TIMESTAMP
          WHERE     PID = patient_id
                AND ALERT_ID = ALT_id
                AND STATUS = 'A'
                AND CLEARED_AT IS NULL;

         INSERT INTO JSHARDA.HAS_ALERT (JSHARDA.HAS_ALERT.ALERT_ID,
                                        JSHARDA.HAS_ALERT.CLEARED_AT,
                                        JSHARDA.HAS_ALERT.CLEARED_BY_ID,
                                        JSHARDA.HAS_ALERT.GENERATED_AT,
                                        JSHARDA.HAS_ALERT.PID,
                                        JSHARDA.HAS_ALERT.STATUS)
              VALUES (ALT_id,
                      NULL,
                      NULL,
                      recorded_on,
                      patient_id,
                      'A');



         RET := 1;
      ELSE
         RET := 2;
      END IF;

      SELECT a.ALERT_ID
        INTO a_id
        FROM JSHARDA.ALERT a
       WHERE a.DID = disease_id AND a.ALERT_TYPE = 'L';

      CLEAR_ALERT_FOR_PATIENT (patient_id,
                               a_id,
                               added_by,
                               return_code_for_clear_alert);

      INSERT INTO JSHARDA.HAS_DISEASE_OBSERVATION (PID,
                                                   DID,
                                                   VAL,
                                                   OBSERVED_ON,
                                                   RECORDED_ON,
                                                   IS_VIOLATED,
                                                   ADDED_BY)
           VALUES (patient_id,
                   disease_id,
                   val,
                   observed_on,
                   recorded_on,
                   is_violated,
                   added_by);
   END LOOP;
END;
/
