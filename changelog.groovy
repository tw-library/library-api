databaseChangeLog {
  changeSet(id: '1457123372258-1', author: 'eferreir (generated)') {
    dropColumn(columnName: 'donator', tableName: 'copy')
  }

  changeSet(id: '1457123372258-2', author: 'eferreir (generated)') {
    addDefaultValue(columnDataType: 'int', columnName: 'status', defaultValueNumeric: '0', tableName: 'copy')
  }

}
</databaseChangeLog>
