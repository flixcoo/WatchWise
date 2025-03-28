namespace AzureFunctionExample.Model;

public record UserModel(string Username, string PasswordHash)
{
    public static UserModel FromUserTableModel(UserTableModel row)
    {
        return new(
            Username: row.RowKey,
            PasswordHash: row.PasswordHash
        );
    }
};