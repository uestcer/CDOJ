/**
 * System Logger
 * A simple logger for c/c++
 */
#ifndef __LOGGER_H__
#define __LOGGER_H__

/**
 * if you want to open the log, open the LOG_ON option
 */
#define LOG_ON

#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include <cstdarg>
#include <unistd.h>
#include <error.h>
#include <sys/file.h>

bool log_open(const char* fileName);
void log_close();
void log_add_info(const char* info);

extern const int LOG_FATAL;
extern const int LOG_WARNING;
extern const int LOG_MONITOR;
extern const int LOG_NOTICE;
extern const int LOG_TRACE;
extern const int LOG_DEBUG;

#ifdef LOG_ON
#define FM_LOG_FATAL(x...)		log_write(LOG_FATAL, __FILE__, __LINE__, ##x)
#define FM_LOG_WARNING(x...)	log_write(LOG_WARNING, __FILE__, __LINE__, ##x)
#define FM_LOG_MONITOR(x...)	log_write(LOG_MONITOR, __FILE__, __LINE__, ##x)
#define FM_LOG_NOTICE(x...)		log_write(LOG_NOTICE, __FILE__, __LINE__, ##x)
#define FM_LOG_TRACE(x...)		log_write(LOG_TRACE, __FILE__, __LINE__, ##x)
#define FM_LOG_DEBUG(x...)		log_write(LOG_DEBUG, __FILE__, __LINE__, ##x)
#else // LOG_ON
#define FM_LOG_FATAL(x...)
#define FM_LOG_WARNING(x...)
#define FM_LOG_MONITOR(x...)
#define FM_LOG_NOTICE(x...)
#define FM_LOG_TRACE(x...)
#define FM_LOG_DEBUG(x...)
#endif // LOG_ON

#endif // __LOGGER_H__

