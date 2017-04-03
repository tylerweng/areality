import requireLogin from '../middlewares/auth';

const router = app => {
  app.post('/', requireLogin, (req, res) => {
    console.log("creating new badge");
  });
}

export default router;
