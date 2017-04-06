import requireLogin from '../middlewares/require.login';

const router = app => {
  app.post('/', requireLogin, (req, res) => {
    console.log("creating new badge");
  });
}

export default router;
