/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 28/03/2024
 */

import components.OutputBuffer;

public class Main {
    public static void main(String[] args) {
        OutputBuffer buffer = new OutputBuffer();
        VirtualComputer vc = new VirtualComputer(1, buffer);
        BaseWindow bw = new BaseWindow(vc.state.mm.getData(256));
        MainController.main(vc, bw, buffer);
    }
}
