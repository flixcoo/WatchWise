using Azure;
using Azure.Data.Tables;

namespace AzureFunctionExample.Model;

public class FilmTableModel : ITableEntity
{
    public required string PartitionKey { get; set; } // User IName

    public required string RowKey { get; set; } // ID des Films

    public required string Title { get; set; }

    public bool IsLiked { get; set; } = false;

    public DateTimeOffset? Timestamp { get; set; }

    public ETag ETag { get; set; }
}


