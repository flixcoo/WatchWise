using Azure;
using Azure.Data.Tables;

namespace AzureFunctionExample.Model;

public class UserTableModel : ITableEntity
{
    public required string PartitionKey { get; set; } = "Users";

    public required string RowKey { get; set; } // Username

    public required string PasswordHash { get; set; }

    public DateTimeOffset? Timestamp { get; set; }

    public ETag ETag { get; set; }
}