using Azure;
using Azure.Data.Tables;

namespace AzureFunctionExample.Model;

public class FilmCategoryTableModel : ITableEntity
{
    public string PartitionKey { get; set; } = string.Empty; // User Name
    public string RowKey { get; set; } = string.Empty; // "{Kategorie}_{FilmID}"
    public string CategoryName { get; set; } = string.Empty;
    public string FilmId { get; set; } = string.Empty;
    public DateTimeOffset? Timestamp { get; set; }
    public ETag ETag { get; set; }

    // Standard-Konstruktor für Azure Table Storage (erforderlich!)
    public FilmCategoryTableModel() { }

    // Benutzerdefinierter Konstruktor für einfaches Erstellen
    public FilmCategoryTableModel(string userName, string categoryName, string filmId)
    {
        PartitionKey = userName;
        CategoryName = categoryName;
        FilmId = filmId;
        RowKey = $"{categoryName}_{filmId}"; // Automatische Generierung des RowKeys
    }
}


