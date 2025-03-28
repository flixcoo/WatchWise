using Azure;
using Azure.Data.Tables;

namespace AzureFunctionExample.Model;

public class CategoryTableModel : ITableEntity
{
    public required string PartitionKey { get; set; } // User Name

    public required string RowKey { get; set; } // Kategoriename als ID

    public string? Description { get; set; }

    public DateTimeOffset? Timestamp { get; set; }

    public ETag ETag { get; set; }
}