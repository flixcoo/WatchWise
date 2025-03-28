namespace AzureFunctionExample.Model;

public record CategoryModel(string CategoryName, string? Description)
{
    public static CategoryModel FromCategoryTableModel(CategoryTableModel row)
    {
        return new(
            CategoryName: row.RowKey,
            Description: row.Description
        );
    }
}