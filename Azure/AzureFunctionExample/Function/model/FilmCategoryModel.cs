namespace AzureFunctionExample.Model;
public record FilmCategoryModel(string CategoryName, string FilmId)
{
    public static FilmCategoryModel FromFilmCategoryTableModel(FilmCategoryTableModel row)
    {
        return new(
            CategoryName: row.CategoryName,
            FilmId: row.FilmId
        );
    }
}