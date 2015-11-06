package niftijio.niftijio;

import java.io.IOException;
import java.io.PrintWriter;

public class Exec
{
    private static void printUsage() {
        System.out.println("Usage: niftijio input.nii.gz [output_filename]");
        System.out.println("Description: read and observe a NIFTI volume");
    }
    
    public static void main(String[] args)
    {
        try
        {
            if (args.length == 0 || args.length == 1 && "--help".equals(args[0])) {
                printUsage();
                return;
            }
            
            NiftiVolume volume = null;
            NiftiHeader header;
            if (args.length > 1) {
                volume = NiftiVolume.read(args[0]);
                header = volume.header;
            } else  {
                header = NiftiHeader.read(args[0]);
            }

            int nx = header.dim[1];
            int ny = header.dim[2];
            int nz = header.dim[3];
            int dim = header.dim[4];

            if (dim == 0)
                dim = 1;

            try (PrintWriter out = (volume != null) ? new PrintWriter(args[1]) : new PrintWriter(System.out)) {
                out.println("volume ");
                out.print("dimensions: ");
                out.println(nx + " " + ny + " " + nz + " " + dim);
                header.dump(out);
                out.println();
                if (volume != null) {
                    out.println("data:");
                    for (int d = 0; d < dim; d++) {
                        out.printf("----BEGIN dim=%-2d----", d);
                        for (int k = 0; k < nz; k++) {
                            for (int j = 0; j < ny; j++) {
                                for (int i = 0; i < nx; i++)
                                    out.print(volume.data.get(i,j,k,d) + " ");
                                out.println();
                            }
                            out.println("--------");
                        }
                        out.printf("---- END dim=%-2d ----", d);
                    }
                }
                out.println();
            }
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }

    }
}
