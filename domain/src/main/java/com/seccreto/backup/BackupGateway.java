package com.seccreto.backup;

import com.seccreto.message.Message;
import com.seccreto.pagination.Pagination;
import com.seccreto.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface BackupGateway {
  
  Backup createBackup(BackupFilter backupFilter, Backup backup);
  
  boolean restoreBackup(BackupID backupId);
  
  void deleteBackup(BackupID backupId);

  Pagination<Message> listBackups(SearchQuery searchQuery);
  
}
