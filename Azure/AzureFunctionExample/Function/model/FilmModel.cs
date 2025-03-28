namespace AzureFunctionExample.Model;
public record FilmModel(string Title, string FilmId)
{
    public static FilmModel FromFilmTableModel(FilmTableModel row)
    {
        return new(
            FilmId: row.RowKey,
            Title: row.Title
        );
    }
}