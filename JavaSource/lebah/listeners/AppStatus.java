/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.listeners;

public class AppStatus {
    
    public static void main(String[] args) {
        AppWatcher w = AppWatcher.getInstance();
        
        System.out.println(w.getFreeMem() + "/" + w.getTotalMem());
    }

}
