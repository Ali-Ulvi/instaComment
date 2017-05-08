import javafx.util.Pair;

/**
 * Created by AUT via kafein on 06.05.2017.
 */
public class selenyumThread extends Thread {
    public void run() {
        Instagram insta = new Instagram();
        insta.setComment(args.comm);
        insta.user = vb.user.getText();
        insta.pw = vb.pw.getText();
        boolean first = true;
        Pair<String, Integer> url;
        while (!(url = args.getUrl()).getKey().contentEquals("-1")) {
            insta.setUrl(url.getKey().trim());
            if (!first) insta.login = false;
            try {
                insta.test_Login();

                vb.textArea2.appendText(url.getValue() + ": OK  " + insta.getUrl() + "\n");
                first = false;

            } catch (Exception e1) {

                vb.textArea2.appendText(url.getValue() + ": FAILED  " + insta.getUrl() + "\n");
                vb.textArea2.appendText(e1.getMessage());
                if (e1.getMessage().contains("8 second")) {
                    vb.textArea2.appendText(" *Instagram may have banned you from commenting\n");
                }
                else
                vb.textArea2.appendText("\n");
            }
            vb.done();
        }

    }

    VBoxExample.startComment args;
    VBoxExample vb;

    public selenyumThread(VBoxExample.startComment args, VBoxExample vb) {
        this.args = args;
        this.vb = vb;
    }
}
