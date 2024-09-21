package vn.nhom2eaut.bookchefk12;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import vn.nhom2eaut.bookchefk12.models.Comment;
import vn.nhom2eaut.bookchefk12.models.Favorite;
import vn.nhom2eaut.bookchefk12.repositories.RecipeRepository;
import vn.nhom2eaut.bookchefk12.repositories.UserRepository;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.UserDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTestApplication {
    private UserRepository userRepository = new UserRepository();
    private RecipeRepository recipeRepository = new RecipeRepository();

    @Ignore
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("vn.nhom2eaut.bookchefk12", appContext.getPackageName());
    }

    @Test
    public void testLoginSuccess() throws InterruptedException {
        // Kiểm thử với thông tin tài khoản có trong hệ thống
        String username = "admin23";
        String password = "admin@";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.login(username, password, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                // Kết quả trả về khác null
                assertNotNull(result);
                // username từ kết quả trả về bằng với username truyền vào
                assertEquals( result.getUsername(), username);
                latch.countDown();  // Giải phóng latch khi thành công
            }
            @Override
            public void onFailure(Exception e) {
                fail("Login thất bại: " + e.getMessage());
                latch.countDown();  // Giải phóng latch khi thất bại
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testLoginFail() throws InterruptedException {
        // Sử dụng thông tin đăng nhập sai
        String username = "uhiahwks";
        String password = "2803";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.login(username, password, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                // Kiểm tra đăng nhập không thành công
                fail("Đăng nhập thành công với thông tin sai, đáng lẽ phải thất bại!");
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                // Kết quả mong muốn
                assertTrue(e.getMessage().contains("Tài khoản hoặc mật khẩu không chính xác"));
                latch.countDown();  // Giải phóng latch khi thất bại
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testRegisterSuccess() throws InterruptedException {
        String username = "uhiahwks";
        String password = "2803";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.register(username, password, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Kiểm tra đăng ký thành công
                assertEquals(result, true);
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                fail("Đăng ký thất bại: " + e.getMessage());
                latch.countDown();  // Giải phóng latch khi thất bại
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testRegisterFail() throws InterruptedException {
        String username = "admin";
        String password = "2803";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.register(username, password, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Đăng ký thành công
                fail("Trường hợp đăng ký này phải thất bại");
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                assertTrue(e.getMessage().contains("Username đã được sử dụng"));
                latch.countDown();  // Giải phóng latch khi thất bại
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testAddRecipeToFavorite() throws InterruptedException {
        Favorite favorite = new Favorite();     // Tạo đối tượng favorite
        favorite.setUserId("uFby3sez9j03IVdbWMmd");
        favorite.setRecipeId("PJdcnvHFSfZ0xCp1hWge");
        favorite.setAddedAt("2024-09-20 21:59:14");

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.addRecipeToFavorite(favorite, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Thêm thành công
                assertEquals(result, true);
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                // Thêm thất bại
                fail("Thêm công thức vào danh sách yêu thích thất bại");
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testRemoveRecipeInFavorite() throws InterruptedException {
        String recipeId = "ZbKYqzyGhu9NSb8OM0ZM";
        String userId = "gvvzh5yb3L4IH5DFplpT";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.removeRecipeInFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Xóa thành công
                assertEquals(result, true);
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                fail("Xóa công thức khỏi danh sách yêu thích thất bại");
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testAddComment() throws InterruptedException {
        Comment comment = new Comment();    // Tạo đối tượng comment
        comment.setUserId("UO55K1LSUrspSizFjFis");
        comment.setRecipeId("PJdcnvHFSfZ0xCp1hWge");
        comment.setRating(4);
        comment.setCreatedAt("2024-09-20 20:34:5");
        comment.setContent("Món này ăn rất ngon");

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        userRepository.addComment(comment, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Thêm thành công
                assertEquals(result, true);
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                // Thêm không thành công
                fail("Thêm đánh giá thất bại: " + e.getMessage());
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }

    @Test
    public void testSearchRecipeByTitle() throws InterruptedException {
        String searchStr = "Gà";

        CountDownLatch latch = new CountDownLatch(1);  // Để đợi kết quả từ callback
        recipeRepository.searchByTitle(searchStr, new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                // Tìm kiếm thành công kết quả mong đợi trả về danh sách độ dài = 2
                assertEquals(result.size(), 2);
                latch.countDown();
            }
            @Override
            public void onFailure(Exception e) {
                // Xảy ra lỗi
                fail("Lỗi khi tìm kiếm công thức: " + e.getMessage());
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);  // Chờ callback trong tối đa 5 giây
    }
}