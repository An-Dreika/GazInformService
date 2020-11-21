package com.testtask;

import com.testtask.exception.DataException;
import com.testtask.service.*;
import com.testtask.bean.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.*;
import org.mockito.quality.Strictness;

import java.sql.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    static final String GENERAL_USER_FIRST_NAME = "Alexander";
    static final String GENERAL_USER_LAST_NAME = "Petrov";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    ConnectorDB jdbcConnection;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;
    private boolean isUpdated = false;
    private UserService service;

    @Before
    public void setUp() throws SQLException {
        when(jdbcConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        when(preparedStatement.executeQuery(anyString())).thenReturn(resultSet);
        configureResultSet();

        service = new UserService(jdbcConnection);
    }

    private void configureResultSet() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("lastName")).then(invocationOnMock -> {
            return isUpdated ? "Faked" : GENERAL_USER_LAST_NAME;
        });
        when(resultSet.getString("firstName")).thenReturn(GENERAL_USER_FIRST_NAME);
    }

    private void configureUpdatedStatement() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
    }

    @Test
    public void getNotEmptyUserByFirstName() throws DataException, SQLException {
        configureResultSet();
        Optional<User> user = service.getAccountByFirstName(GENERAL_USER_FIRST_NAME);
        Assert.assertTrue(user.isPresent());
    }

    @Test
    public void validateLastName() throws DataException, SQLException {
        configureResultSet();
        Optional<User> user = service.getAccountByFirstName(GENERAL_USER_FIRST_NAME);
        Assert.assertEquals(GENERAL_USER_LAST_NAME, user.get().getLastName());
    }

    @Test
    public void validateFirstName() throws DataException, SQLException {
        configureResultSet();
        Optional<User> user = service.getAccountByFirstName(GENERAL_USER_FIRST_NAME);
        Assert.assertEquals(GENERAL_USER_FIRST_NAME, user.get().getFirstName());
    }

    @Test
    public void getNotExistedUserByFirstName() throws DataException, SQLException {
        when(resultSet.next()).thenReturn(false);
        Optional<User> user = service.getAccountByFirstName("Fantom");
        Assert.assertFalse(user.isPresent());
    }

    @Test
    public void updateLastName() throws DataException, SQLException {
        configureResultSet();
        configureUpdatedStatement();
        Optional<User> userOriginal = service.getAccountByFirstName(GENERAL_USER_FIRST_NAME);
        Assert.assertEquals(GENERAL_USER_LAST_NAME, userOriginal.get().getLastName());

        service.updateLastName(GENERAL_USER_FIRST_NAME, "Faked");
        isUpdated = true;
        Optional<User> updatedUser = service.getAccountByFirstName(GENERAL_USER_FIRST_NAME);
        Assert.assertEquals("Faked", updatedUser.get().getLastName());
    }
}