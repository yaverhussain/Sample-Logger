package logger;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by hussaiy on 4/07/2017.
 */
public class LoggerInAction {
  static {
    System.setProperty("log4j.configurationFile", "log4j2.xml");
  }

  final static Logger logger = Logger.getLogger(LoggerInAction.class);
  private final static String INFO_KEY = "info";
  private final static String WARN_KEY = "warn";
  private final static String ERROR_KEY = "error";
  private final static String DEBUG_KEY = "debug";
  private static final long JOB_SLEEP_TIME_MILLIS = 20000;
  private static final long LOG_DISPLAY_INTERVAL_MILLIS = 3000;
  private static final long MAXIMUM_LOGS_PER_JOB_RUN = 5000;

  public static void main(String[] args) {
    LoggerInAction logger = new LoggerInAction();

    //source.setLocation(logConfigurationFile);

    try {
      if (args.length > 0) { // first argument is file name e.g -> "sourcelog.txt"
        logger.generateLogs(args[0]);
      } else {
        logger.generateLogs();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void suspendCurrentThread(long sleepTime) {
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException ex) {
      // do nothing.
    }
  }

  // can be called from outside
  public void generateLogs() throws Exception {
    Thread t = new Thread(new Runnable() {
      Random rand = new Random();
      final Map<String, List<String>> dummyLog = retrieveDummyLog();
      final List<String> keys = new ArrayList(dummyLog.keySet());

      public void run() {
        while (true) {
          int count = 0;
          while (count++ < MAXIMUM_LOGS_PER_JOB_RUN) {
            String key = keys.get(rand.nextInt(keys.size()));
            List<String> logMessages = dummyLog.get(key);
            log(logMessages.get(rand.nextInt(logMessages.size())), key);
            suspendCurrentThread(LOG_DISPLAY_INTERVAL_MILLIS);
          }
          suspendCurrentThread(JOB_SLEEP_TIME_MILLIS);
        }
      }
    });
    t.start();
  }

  // can be called from outside (Provide file name to take log messages from)
  public void generateLogs(final String fileName) throws Exception {
    Thread t = new Thread(new Runnable() {
      Random rand = new Random();
      List<String> dummyLog = retrieveDummyLog(fileName);
      List<String> keys = Arrays.asList(INFO_KEY, DEBUG_KEY, WARN_KEY, ERROR_KEY);

      public void run() {
        while (true) {
          int count = 0;
          while (count++ < MAXIMUM_LOGS_PER_JOB_RUN) {
            log(dummyLog.get(rand.nextInt(dummyLog.size())), keys.get(rand.nextInt(keys.size())));
            suspendCurrentThread(LOG_DISPLAY_INTERVAL_MILLIS);
          }
          suspendCurrentThread(JOB_SLEEP_TIME_MILLIS);
        }
      }
    });
    t.start();
    System.in.read();
    t.stop();
  }

  private void log(final String logMessage, String key) {
    switch (key) {
      case INFO_KEY:
        logger.info(logMessage);
        break;
      case DEBUG_KEY:
        logger.debug(logMessage);
        break;
      case WARN_KEY:
        logger.warn(logMessage);
        break;
      case ERROR_KEY:
        logger.error(logMessage);
        break;
      default:
        logger.debug(logMessage);
    }
  }

  private List<String> retrieveDummyLog(String fileName) throws IOException {
    List<String> lines = new ArrayList<>();
    InputStream fis = new FileInputStream(fileName);
    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
    BufferedReader br = new BufferedReader(isr);
    String line;
    while ((line = br.readLine()) != null) {
      lines.add(line);
    }
    return lines;
  }

  private Map<String, List<String>> retrieveDummyLog() {
    Map<String, List<String>> logMap = new HashMap<>();

    // INFO
    List<String> logMessages = new ArrayList<>();
    logMessages.add("Finish {method:POST, uri:/fmit-common-service/services/unfinished-business/unfinished-business-items");
    logMessages.add("nz.co.fmit.generic.application.service~~~[000] Request_nz.co.fmit.generic.application.configuration.ReferenceConfigurationManager.lastUpdated 87.058 ms (self:0.019");
    logMessages.add("nz.co.fmit.generic.application.service~~~Request cache had 0 request(s); 0 hits, 0 misses; 0.00% hit rate, 0.00% mis rate.");
    logMessages.add("nz.co.fmit.generic.application.domain.db.impl.BusinessServicePersisterImpl~~~logPersisterSaveEvent not logged, no service instance found");
    logMessages.add("nz.co.fmit.framework.messaging.http.CachingHttpRequester~~~Returning response for ");
    logMessages.add("nz.co.fmit.generic.application.service~~~transaction lastUpdated (BaseApplicationContext: registerCode=COMPANIES, businessService=null, ");
    logMap.put(INFO_KEY, logMessages);

    // WARN
    logMessages = new ArrayList<>();
    logMessages.add("Multiple Spring beans found for type interface org.springframework.transaction.PlatformTransactionManager returning the");
    logMessages.add("org.jbpm.pvm.internal.env.SpringContext~~~Multiple Spring beans found for type interface org.springframework.transaction.PlatformTransactionManager returning the");
    logMessages.add("org.springframework.beans.factory.config.PropertyPlaceholderConfigurer   Could not load properties from class path resource ");
    logMessages.add("[nz.co.fmit.enterprise.webui.env.properties]: class path resource [nz.co.fmit.enterprise.webui.env.properties] cannot be opened because it does not exist");
    logMessages.add("org.springframework.security.config.http.MatcherType   'path-type' is deprecated. Please use 'request-matcher' instead.");
    logMessages.add("org.springframework.aop.framework.CglibAopProxy   Unable to proxy method [public final void org.springframework.dao.support.DaoSupport.afterPropertiesSet()");
    logMap.put(WARN_KEY, logMessages);

    // ERROR
    logMessages = new ArrayList<>();
    logMessages.add("weblogic.kernel.Default (self-tuning)'~nz.co.fmit.security.service.web.Log4jConfigListener~log4j.xml not found");
    logMessages.add("Caused by: java.sql.SQLException: Internal error: Cannot obtain XAConnection weblogic.common.resourcepool.ResourceDeadException: ");
    logMessages.add("nz.co.fmit.common.services.fax.FaxPollService   The Companies Authority path () does not exist.");
    logMessages.add("ApplicationEvent: org.springframework.context.event.ContextRefreshedEvent[source=Root WebApplicationContext: ]");
    logMessages.add("nz.co.fmit.enterprise.service.business.configuration.Log4jConfigListener   log4j.xml not found");
    logMessages.add("nz.co.fmit.generic.application.configuration.impl.ApplicationConfigurationBean~~~Service form items are not grouped into trees");
    logMap.put(ERROR_KEY, logMessages);

    // DEBUG
    logMessages = new ArrayList<>();
    logMessages.add("FRL records for DelegatedAuthorityIndividualRecords not processed as they already exist for the year.");
    logMessages.add("About to run the diagnostic test on annual filings to make sure it finishes successfully.");
    logMessages.add("starting updateFrComplianceRecord() for reminder: testReminder, Entity: testEntity");
    logMessages.add("Not performing sendDataprintExtract() for testReminder as it has already been processed\"");
    logMessages.add("Generating first overdue reminders");
    logMessages.add("Created 2 FRL records (createFRLForDelegatedAuthorityIndividualRecords) for state active");
    logMap.put(DEBUG_KEY, logMessages);

    return logMap;
  }
}
