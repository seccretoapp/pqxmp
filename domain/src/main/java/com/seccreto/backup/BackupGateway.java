package com.seccreto.backup;

import com.seccreto.message.Message;
import com.seccreto.pagination.Pagination;
import com.seccreto.pagination.SearchQuery;

public interface BackupGateway {
  
  Backup createBackup(BackupFilter backupFilter, Backup backup);
  
  boolean restoreBackup(BackupID backupId);
  
  void deleteBackup(BackupID backupId);

  Pagination<Message> listBackups(SearchQuery searchQuery);
  
}
